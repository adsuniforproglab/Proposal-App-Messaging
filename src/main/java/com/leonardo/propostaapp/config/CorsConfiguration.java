package com.leonardo.propostaapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for Cross-Origin Resource Sharing (CORS). Allows controlled
 * access to resources from different origins.
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.max-age}")
    private long maxAge;

    /**
     * Configures CORS mappings for the application. Restricts origins, methods,
     * and headers for security.
     * 
     * @param registry The CORS registry
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(maxAge);
    }
}
