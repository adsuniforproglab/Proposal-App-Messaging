package com.leonardo.propostaapp.dto;

import java.text.NumberFormat;

import com.leonardo.propostaapp.entity.Proposal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for proposal response data. Used for transferring proposal information
 * back to clients.
 */
@Schema(description = "Represents proposal data in responses")
public record ProposalResponse(
        @Schema(description = "User's first name (uppercase)", example = "JOHN") String name,

        @Schema(description = "User's last name (uppercase)", example = "DOE") String lastName,

        @Schema(description = "User's telephone number", example = "(11) 98765-4321") String phoneNumber,

        @Schema(description = "User's CPF (Brazilian tax ID)", example = "123.456.789-00") String cpf,

        @Schema(description = "User's financial income", example = "5000.00") Double financialIncome,

        @Schema(description = "Formatted proposal value", example = "$10,000.00") String proposalValueFormatted,

        @Schema(description = "Payment term in months", example = "36") int paymentTerm,

        @Schema(description = "Whether the proposal was approved") Boolean approved,

        @Schema(description = "Additional observations about the proposal") String observation) {

    /**
     * Constructs a response DTO from a Proposal entity.
     *
     * @param proposal The proposal entity
     */
    public ProposalResponse(Proposal proposal) {
        this(
                proposal.getUser().getName().toUpperCase(),
                proposal.getUser().getLastName().toUpperCase(),
                proposal.getUser().getPhoneNumber(),
                proposal.getUser().getCpf(),
                proposal.getUser().getFinancialIncome(),
                NumberFormat.getCurrencyInstance().format(proposal.getProposalValue()),
                proposal.getPaymentTerm(),
                proposal.getApproved(),
                proposal.getObservation());
    }
}
