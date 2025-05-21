package com.leonardo.propostaapp.service;

import com.leonardo.propostaapp.entity.Proposal;
import com.leonardo.propostaapp.repository.ProposalRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProposalUpdateService {

    private final ProposalRepository proposalRepository;

    public void updateProposalStatus(Proposal proposal) {
        proposalRepository.save(proposal);
        log.info("Updated proposal status for ID: {}", proposal.getId());
    }
}
