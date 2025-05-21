package com.leonardo.propostaapp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for RabbitMQ messaging. Sets up exchanges, queues, bindings,
 * and message converters.
 */
@Configuration
public class RabbitConfiguration {

    // Queue names
    private static final String PENDING_PROPOSAL_CREDIT_ANALYSIS_QUEUE = "pending-proposal.ms-credit-analysis";
    private static final String PENDING_PROPOSAL_DLQ = "pending-proposal.dlq";
    private static final String PENDING_PROPOSAL_NOTIFICATION_QUEUE = "pending-proposal.ms-notification";
    private static final String COMPLETED_PROPOSAL_QUEUE = "completed-proposal.ms-proposal";
    private static final String COMPLETED_PROPOSAL_NOTIFICATION_QUEUE = "completed-proposal.ms-notification";

    // Exchange names
    private static final String PENDING_PROPOSAL_DLX = "pending-proposal-dlx.ex";

    @Value("${rabbitmq.pending-proposal.exchange}")
    private String pendingProposalExchange;

    @Value("${rabbitmq.completed-proposal.exchange}")
    private String completedProposalExchange;

    /**
     * Queue configuration section
     */

    /**
     * Creates a queue for pending proposals to be analyzed by the credit
     * analysis service. Includes dead letter and priority configuration.
     */
    @Bean
    Queue createPendingProposalQueueForCreditAnalysis() {
        return QueueBuilder.durable(PENDING_PROPOSAL_CREDIT_ANALYSIS_QUEUE)
                .deadLetterExchange(PENDING_PROPOSAL_DLX)
                .maxPriority(10)
                .build();
    }

    /**
     * Creates a dead letter queue for failed proposal messages.
     */
    @Bean
    Queue createPendingProposalDeadLetterQueue() {
        return QueueBuilder.durable(PENDING_PROPOSAL_DLQ).build();
    }

    /**
     * Creates a queue for pending proposal notifications.
     */
    @Bean
    Queue createPendingProposalNotificationQueue() {
        return QueueBuilder.durable(PENDING_PROPOSAL_NOTIFICATION_QUEUE).build();
    }

    /**
     * Creates a queue for completed proposals to be processed by the proposal
     * app.
     */
    @Bean
    Queue createCompletedProposalQueueForProposal() {
        return QueueBuilder.durable(COMPLETED_PROPOSAL_QUEUE).build();
    }

    /**
     * Creates a queue for completed proposal notifications.
     */
    @Bean
    Queue createCompletedProposalNotificationQueue() {
        return QueueBuilder.durable(COMPLETED_PROPOSAL_NOTIFICATION_QUEUE).build();
    }

    /**
     * Exchange configuration section
     */

    /**
     * Creates a dead letter exchange for handling failed messages.
     */
    @Bean
    FanoutExchange deadLetterExchange() {
        return ExchangeBuilder.fanoutExchange(PENDING_PROPOSAL_DLX).build();
    }

    /**
     * Creates a fanout exchange for pending proposals.
     */
    @Bean
    FanoutExchange createFanoutExchangePendingProposal() {
        return ExchangeBuilder.fanoutExchange(pendingProposalExchange).build();
    }

    /**
     * Creates a fanout exchange for completed proposals.
     */
    @Bean
    FanoutExchange createFanoutExchangeCompletedProposal() {
        return ExchangeBuilder.fanoutExchange(completedProposalExchange).build();
    }

    /**
     * Binding configuration section
     */

    /**
     * Binds the dead letter queue to the dead letter exchange.
     */
    @Bean
    Binding createDeadLetterBinding() {
        return BindingBuilder.bind(createPendingProposalDeadLetterQueue()).to(deadLetterExchange());
    }

    /**
     * Binds the pending proposal queue for credit analysis to the pending
     * proposal exchange.
     */
    @Bean
    Binding createBindingPendingProposalMSCreditAnalysis() {
        return BindingBuilder.bind(createPendingProposalQueueForCreditAnalysis())
                .to(createFanoutExchangePendingProposal());
    }

    /**
     * Binds the pending proposal notification queue to the pending proposal
     * exchange.
     */
    @Bean
    Binding createBindingPendingProposalMSNotification() {
        return BindingBuilder.bind(createPendingProposalNotificationQueue())
                .to(createFanoutExchangePendingProposal());
    }

    /**
     * Binds the completed proposal queue to the completed proposal exchange.
     */
    @Bean
    Binding createBindingCompletedProposalMSProposalApp() {
        return BindingBuilder.bind(createCompletedProposalQueueForProposal())
                .to(createFanoutExchangeCompletedProposal());
    }

    /**
     * Binds the completed proposal notification queue to the completed proposal
     * exchange.
     */
    @Bean
    Binding createBindingCompletedProposalMSNotification() {
        return BindingBuilder.bind(createCompletedProposalNotificationQueue())
                .to(createFanoutExchangeCompletedProposal());
    }

    /**
     * RabbitMQ infrastructure configuration section
     */

    /**
     * Creates a RabbitAdmin bean for managing RabbitMQ objects.
     */
    @Bean
    RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Initializes RabbitAdmin when the application is ready.
     */
    @Bean
    ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    /**
     * Creates a Jackson-based message converter for converting objects to/from
     * JSON.
     */
    @Bean
    MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates a RabbitTemplate with our custom message converter.
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
