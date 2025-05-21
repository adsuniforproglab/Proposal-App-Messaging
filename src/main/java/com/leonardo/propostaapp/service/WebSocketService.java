package com.leonardo.propostaapp.service;

import com.leonardo.propostaapp.dto.ProposalResponse;
import com.leonardo.propostaapp.exception.MessagingServiceException;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending real-time notifications via WebSocket.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    private static final String PROPOSALS_DESTINATION = "/proposals";

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Sends a proposal update notification to all connected WebSocket clients.
     *
     * @param proposal The proposal response to send to clients
     * @throws MessagingServiceException if there's an error sending the message
     */
    public void notify(ProposalResponse proposal) {
        try {
            log.debug("Sending WebSocket notification for proposal: {}/{}",
                    proposal.name(), proposal.lastName());

            messagingTemplate.convertAndSend(PROPOSALS_DESTINATION, proposal);

            log.info("Successfully sent WebSocket notification for proposal: {}",
                    proposal.name());
        } catch (MessagingException e) {
            log.error("Failed to send WebSocket notification: {}", e.getMessage(), e);
            throw new MessagingServiceException("Failed to send WebSocket notification", e);
        }
    }
}
