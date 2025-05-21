package com.leonardo.propostaapp.service;

import java.util.List;
import java.util.Optional;

import com.leonardo.propostaapp.dto.ProposalRequest;
import com.leonardo.propostaapp.dto.ProposalResponse;
import com.leonardo.propostaapp.entity.Proposal;
import com.leonardo.propostaapp.exception.MessagingServiceException;
import com.leonardo.propostaapp.exception.ResourceNotFoundException;
import com.leonardo.propostaapp.repository.ProposalRepository;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing proposals.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProposalService {
    private static final int HIGH_PRIORITY = 10;
    private static final int STANDARD_PRIORITY = 5;
    private static final double HIGH_INCOME_THRESHOLD = 10000.0;

    private final ProposalRepository proposalRepository;
    private final NotificationRabbitService notificationRabbitService;

    @Value("${rabbitmq.pending-proposal.exchange}")
    private String pendingProposalExchange;

    /**
     * Creates a new proposal and sends notification to RabbitMQ.
     * 
     * @param proposalRequest The proposal request DTO
     * @return The created proposal entity
     */
    @Transactional
    public Proposal createProposal(ProposalRequest proposalRequest) {
        log.info("Creating new proposal for user {}", proposalRequest.name());

        var user = proposalRequest.toUser();
        var proposal = proposalRequest.toProposal(user);

        int priority = determinePriority(user.getFinancialIncome());
        var savedProposal = proposalRepository.save(proposal);

        log.debug("Saved proposal with ID: {}", savedProposal.getId());

        notifyRabbitWithPriority(savedProposal, priority);
        return savedProposal;
    }

    /**
     * Determines the priority of a proposal based on financial income.
     * 
     * @param financialIncome The financial income of the user
     * @return The priority level (higher means more priority)
     */
    private int determinePriority(Double financialIncome) {
        return financialIncome > HIGH_INCOME_THRESHOLD ? HIGH_PRIORITY : STANDARD_PRIORITY;
    }

    /**
     * Notifies RabbitMQ about a proposal with specified priority.
     * 
     * @param proposal The proposal to send
     * @param priority The message priority
     */
    private void notifyRabbitWithPriority(Proposal proposal, int priority) {
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        };

        notifyRabbit(proposal, messagePostProcessor);
    }

    /**
     * Notifies RabbitMQ about a proposal with priority based on business rules.
     * 
     * @param proposal The proposal to notify about
     * @param messagePostProcessor Processor for setting message properties
     */
    private void notifyRabbit(Proposal proposal, MessagePostProcessor messagePostProcessor) {
        try {
            log.info("Sending proposal {} to RabbitMQ exchange {}", proposal.getId(), pendingProposalExchange);
            notificationRabbitService.notify(proposal, pendingProposalExchange, messagePostProcessor);
        } catch (MessagingServiceException ex) {
            proposalRepository.updateIntegrationStatus(proposal.getId(), false);
            throw ex;
        }
    }

    //**
    /**
     * Updates the status of a proposal in the database.
     * 
     * @param proposal The proposal to update
     */
    public void updateProposalStatus(Proposal proposal) {
        proposalRepository.save(proposal);
        log.info("Updated proposal status for ID: {}", proposal.getId());
    }

    /**
     * Retrieves all proposals in the system.
     * 
     * @return List of proposal response DTOs
     */
    public List<ProposalResponse> getProposals() {
        log.info("Retrieving all proposals");
        return proposalRepository.findAll().stream()
                .map(ProposalResponse::new).toList();
    }

    /**
     * Retrieves a proposal by its ID.
     * 
     * @param id The proposal ID
     * @return Optional containing the proposal if found
     * @throws ResourceNotFoundException if the proposal does not exist
     */
    public Optional<Proposal> getProposalById(Long id) {
        log.info("Retrieving proposal with ID: {}", id);
        Optional<Proposal> proposal = proposalRepository.findById(id);
        if (proposal.isEmpty()) {
            throw new ResourceNotFoundException("Proposal", "id", id);
        }
        return proposal;
    }
}
