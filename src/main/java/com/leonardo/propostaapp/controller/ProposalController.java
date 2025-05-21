package com.leonardo.propostaapp.controller;

import java.util.List;

import com.leonardo.propostaapp.dto.ProposalRequest;
import com.leonardo.propostaapp.dto.ProposalResponse;
import com.leonardo.propostaapp.service.ProposalService;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing proposal operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/proposals")
@Tag(name = "Proposals", description = "APIs for managing financial proposals")
@Validated
public class ProposalController {
    private final ProposalService proposalService;

    @Operation(summary = "Create a new proposal", description = "Creates a new proposal with user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proposal created successfully",
                    content = @Content(schema = @Schema(implementation = ProposalResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ProposalResponse> createProposal(@Valid @RequestBody ProposalRequest proposalRequest,
            UriComponentsBuilder uriBuilder) {
        var proposal = proposalService.createProposal(proposalRequest);
        var responseDTO = new ProposalResponse(proposal);
        var uri = uriBuilder.path("/v1/proposals/{id}").buildAndExpand(proposal.getId()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @Operation(summary = "Get all proposals", description = "Returns all proposals in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of proposals retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ProposalResponse>> getAllProposals() {
        var proposals = proposalService.getProposals();
        return ResponseEntity.ok(proposals);
    }

    @Operation(summary = "Get a specific proposal", description = "Returns a proposal by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proposal retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProposalResponse.class))),
            @ApiResponse(responseCode = "404", description = "Proposal not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProposalResponse> getProposalById(@PathVariable Long id) {
        return proposalService.getProposalById(id)
                .map(proposal -> ResponseEntity.ok(new ProposalResponse(proposal)))
                .orElse(ResponseEntity.notFound().build());
    }
}
