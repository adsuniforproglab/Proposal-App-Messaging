package com.leonardo.propostaapp.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.leonardo.propostaapp.entity.Proposal;
import com.leonardo.propostaapp.exception.MessagingServiceException;
import com.leonardo.propostaapp.repository.ProposalRepository;
import com.leonardo.propostaapp.service.NotificationRabbitService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduler that periodically checks for pending proposals that failed to be
 * integrated and retries sending them to RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PendingProposalScheduler {

    private final ProposalRepository proposalRepository;
    private final NotificationRabbitService notificationRabbitService;

    @Value("${rabbitmq.pending-proposal.exchange}")
    private String pendingProposalExchange;

    /**
     * Scheduled task that looks for proposals that failed to integrate and
     * retries sending them to RabbitMQ. Runs every 10 seconds.
     */
    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    @Transactional(readOnly = true)
    public void retryNonIntegratedProposals() {
        log.debug("Checking for non-integrated proposals");

        List<Proposal> nonIntegratedProposals = proposalRepository.findAllByIntegratedIsFalse();

        if (nonIntegratedProposals.isEmpty()) {
            log.debug("No non-integrated proposals found");
            return;
        }

        log.info("Found {} non-integrated proposals to retry", nonIntegratedProposals.size());

        nonIntegratedProposals.forEach(this::retryProposalIntegration);
    }

    /**
     * Retries integration for a specific proposal.
     * 
     * @param proposal The proposal to retry integration for
     */
    private void retryProposalIntegration(Proposal proposal) {
        try {
            log.info("Retrying to send proposal {} to RabbitMQ", proposal.getId());
            notificationRabbitService.notify(proposal, pendingProposalExchange);
            proposalRepository.updateIntegrationStatus(proposal.getId(), true);
            log.info("Successfully sent proposal {} to RabbitMQ", proposal.getId());
        } catch (MessagingServiceException ex) {
            log.error("Failed to send proposal {} to RabbitMQ: {}",
                    proposal.getId(), ex.getMessage(), ex);
            // Integration status remains false
        }
    }
}
