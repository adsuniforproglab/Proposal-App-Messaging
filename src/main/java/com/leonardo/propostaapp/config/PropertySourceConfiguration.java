package com.leonardo.propostaapp.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

/**
 * Configures a high-priority property source to ensure application-specific
 * database settings override any conflicting environment variables.
 */
@Configuration
public class PropertySourceConfiguration {

    private static final String HIGH_PRIORITY_PROPERTIES = "highPriorityProperties";

    /**
     * Creates a property source with higher priority than environment variables
     * to enforce PostgreSQL configuration.
     *
     * @param environment Spring environment
     * @return The configured environment
     */
    @Bean
    @Primary
    public ConfigurableEnvironment configurableEnvironment(ConfigurableEnvironment environment) {
        MutablePropertySources propertySources = environment.getPropertySources();

        Map<String, Object> highPriorityProperties = new HashMap<>();

        // Force PostgreSQL configuration
        highPriorityProperties.put("spring.datasource.url",
                "jdbc:postgresql://localhost:5432/proposal-app");
        highPriorityProperties.put("spring.datasource.driver-class-name",
                "org.postgresql.Driver");
        highPriorityProperties.put("spring.datasource.username", "myuser");
        highPriorityProperties.put("spring.datasource.password", "secret");

        // Add the property source with highest priority
        propertySources.addFirst(new MapPropertySource(HIGH_PRIORITY_PROPERTIES, highPriorityProperties));

        return environment;
    }
}
