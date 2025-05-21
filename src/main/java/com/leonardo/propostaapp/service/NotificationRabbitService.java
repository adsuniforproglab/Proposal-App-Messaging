package com.leonardo.propostaapp.service;

import com.leonardo.propostaapp.entity.Proposal;
import com.leonardo.propostaapp.exception.MessagingServiceException;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending notifications to RabbitMQ.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationRabbitService {
    private static final String DEFAULT_ROUTING_KEY = "";

    private final RabbitTemplate rabbitTemplate;

    /**
     * Sends a notification to RabbitMQ with custom message properties.
     *
     * @param proposal The proposal to send
     * @param exchange The exchange to send to
     * @param messagePostProcessor Processor for setting message properties
     * @throws MessagingServiceException if there's an error communicating with
     *         RabbitMQ
     */
    public void notify(Proposal proposal, String exchange, MessagePostProcessor messagePostProcessor) {
        try {
            log.debug("Sending proposal {} to exchange {} with message processor", proposal.getId(), exchange);
            rabbitTemplate.convertAndSend(exchange, DEFAULT_ROUTING_KEY, proposal, messagePostProcessor);
            log.info("Successfully sent proposal {} to exchange {}", proposal.getId(), exchange);
        } catch (AmqpException e) {
            throw new MessagingServiceException("Failed to send proposal to message broker", e);
        }
    }

    /**
     * Sends a notification to RabbitMQ with default message properties.
     *
     * @param proposal The proposal to send
     * @param exchange The exchange to send to
     * @throws MessagingServiceException if there's an error communicating with
     *         RabbitMQ
     */
    public void notify(Proposal proposal, String exchange) {
        try {
            log.debug("Sending proposal {} to exchange {}", proposal.getId(), exchange);
            rabbitTemplate.convertAndSend(exchange, DEFAULT_ROUTING_KEY, proposal);
            log.info("Successfully sent proposal {} to exchange {}", proposal.getId(), exchange);
        } catch (AmqpException e) {
            throw new MessagingServiceException("Failed to send proposal to message broker", e);
        }
    }
}
