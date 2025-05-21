package com.leonardo.propostaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Proposal App. Handles proposal management and
 * integration with messaging systems.
 */
@SpringBootApplication
@EnableScheduling
public class ProposalAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProposalAppApplication.class, args);
	}

}
