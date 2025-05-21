package com.leonardo.propostaapp.listener;

import com.leonardo.propostaapp.dto.ProposalResponse;
import com.leonardo.propostaapp.entity.Proposal;
import com.leonardo.propostaapp.service.ProposalService;
import com.leonardo.propostaapp.service.WebSocketService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener for completed proposal messages from RabbitMQ. Processes completed
 * proposals and sends notifications via WebSocket.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CompletedProposalListener {

    private final ProposalService proposalService;
    private final WebSocketService webSocketService;

    /**
     * Receives completed proposal messages from RabbitMQ. Updates the proposal
     * in the database and notifies clients via WebSocket.
     *
     * @param proposal The completed proposal received from RabbitMQ
     */
    @RabbitListener(queues = "${rabbitmq.queue.completed.proposal}")
    public void handleCompletedProposal(Proposal proposal) {
        log.info("Received completed proposal with ID: {}", proposal.getId());

        try {
            proposalService.updateProposalStatus(proposal);
            notifyClientsViaWebSocket(proposal);
            log.info("Successfully processed proposal: {}", proposal.getId());
        } catch (Exception e) {
            log.error("Error processing completed proposal with ID: {}: {}",
                    proposal.getId(), e.getMessage(), e);
        }
    }

    /**
     * Notifies connected clients about proposal update via WebSocket.
     *
     * @param proposal The updated proposal
     */
    private void notifyClientsViaWebSocket(Proposal proposal) {
        log.debug("Notifying clients about proposal: {}", proposal.getId());
        var response = new ProposalResponse(proposal);
        webSocketService.notify(response);
        log.info("Clients notified about proposal: {}", proposal.getId());
    }
}
