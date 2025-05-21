package com.leonardo.propostaapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration for WebSocket communication. Sets up STOMP endpoints and
 * message broker for real-time communication.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private static final String WEBSOCKET_ENDPOINT = "/ws";
    private static final String PROPOSALS_TOPIC = "/proposals";
    private static final String FRONTEND_ORIGIN = "http://localhost:3000";

    /**
     * Registers STOMP endpoints for WebSocket communication.
     * 
     * @param registry The endpoint registry
     */
    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_ENDPOINT)
                .setAllowedOrigins(FRONTEND_ORIGIN)
                .withSockJS();
    }

    /**
     * Configures the message broker for WebSocket communication.
     * 
     * @param registry The message broker registry
     */
    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker(PROPOSALS_TOPIC);
    }
}
