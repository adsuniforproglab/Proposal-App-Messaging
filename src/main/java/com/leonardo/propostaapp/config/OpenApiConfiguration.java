package com.leonardo.propostaapp.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuration class for OpenAPI documentation.
 */
@Configuration
public class OpenApiConfiguration {

        @Value("${server.servlet.context-path:}")
        private String contextPath;

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Proposal App API")
                                                .description("API for managing proposals and integration with messaging systems")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("Leonardo")
                                                                .url("https://github.com/leonardo"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("Project Documentation")
                                                .url("https://github.com/leonardo/proposal-app"))
                                .servers(List.of(
                                                new Server()
                                                                .url(contextPath)
                                                                .description("Local server")));
        }

}
