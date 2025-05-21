package com.leonardo.propostaapp.dto;

import com.leonardo.propostaapp.entity.Proposal;
import com.leonardo.propostaapp.entity.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * DTO for receiving proposal creation requests.
 */
public record ProposalRequest(
                @NotBlank(message = "Name is required") String name,

                @NotBlank(message = "Last name is required") String lastName,

                @NotBlank(message = "CPF is required") @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}$",
                                message = "CPF must be in a valid format") String cpf,

                @NotBlank(message = "Telephone is required") @Pattern(
                                regexp = "^\\(?\\d{2}\\)?[\\s\\-]?\\d{4,5}[\\-]?\\d{4}$",
                                message = "Telephone must be in a valid format") String telephone,

                @NotNull(message = "Financial income is required") @Positive(
                                message = "Financial income must be positive") Double financialIncome,

                @NotNull(message = "Proposal value is required") @Positive(
                                message = "Proposal value must be positive") Double proposalValue,

                @NotNull(message = "Payment term is required") @Min(value = 1,
                                message = "Payment term must be at least 1") int paymentTerm) {

        /**
         * Converts this DTO to a Proposal entity.
         *
         * @param user The user associated with this proposal
         * @return A new Proposal entity
         */
        public Proposal toProposal(User user) {
                return Proposal.builder()
                                .proposalValue(this.proposalValue())
                                .paymentTerm(this.paymentTerm())
                                .approved(null)
                                .integrated(true)
                                .observation(null)
                                .user(user)
                                .build();
        }

        /**
         * Converts this DTO to a User entity.
         *
         * @return A new User entity
         */
        public User toUser() {
                return User.builder()
                                .name(this.name())
                                .lastName(this.lastName())
                                .cpf(this.cpf())
                                .telephone(this.telephone())
                                .financialIncome(this.financialIncome())
                                .build();
        }
}
