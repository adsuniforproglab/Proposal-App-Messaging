package com.leonardo.propostaapp.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration for PostgreSQL database connections. Provides explicit control
 * over datasource configuration to avoid driver/URL mismatches.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {

    private static final String APP_PREFIX = "spring.datasource";
    private static final String POSTGRESQL_PREFIX = "jdbc:postgresql";

    private final Environment environment;

    /**
     * Creates the application's primary PostgreSQL data source with validation
     * to ensure driver and URL compatibility.
     *
     * @return Configured PostgreSQL DataSource
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        // Try app-specific properties first to avoid conflicts
        String url = environment.getProperty(APP_PREFIX + ".url");
        String driverClassName = environment.getProperty(APP_PREFIX + ".driver-class-name");
        String username = environment.getProperty(APP_PREFIX + ".username");
        String password = environment.getProperty(APP_PREFIX + ".password");

        // Fall back to standard properties if app-specific ones aren't defined
        if (url == null) {
            url = environment.getProperty("spring.datasource.url");
            driverClassName = environment.getProperty("spring.datasource.driver-class-name");
            username = environment.getProperty("spring.datasource.username");
            password = environment.getProperty("spring.datasource.password");

            log.info(
                    "Using standard Spring datasource properties. Consider using '{}' prefixed properties to avoid conflicts.",
                    APP_PREFIX);
        }

        validatePostgresConfiguration(url, driverClassName);

        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(username)
                .password(password)
                .build();
    }

    /**
     * Validates that the configuration is compatible with PostgreSQL.
     *
     * @param url The database URL
     * @param driverClassName The database driver class name
     */
    private void validatePostgresConfiguration(String url, String driverClassName) {
        if (url == null || driverClassName == null) {
            log.error("Database URL or driver class name is null");
            throw new IllegalStateException("Database URL or driver class name is null");
        }

        if (!url.startsWith(POSTGRESQL_PREFIX)) {
            log.error("Invalid PostgreSQL URL: {}. This application requires PostgreSQL.", url);
            throw new IllegalStateException(
                    "This application requires a PostgreSQL database. Found URL: " + url +
                            ". If you have environment variables from another project, please unset them or use " +
                            "application-specific properties with '" + APP_PREFIX + "' prefix.");
        }

        if (!driverClassName.contains("postgresql")) {
            log.error("Invalid PostgreSQL driver: {}", driverClassName);
            throw new IllegalStateException(
                    "This application requires the PostgreSQL driver. Found: " + driverClassName);
        }

        log.info("PostgreSQL configuration validated successfully: {}", url);
    }
}
