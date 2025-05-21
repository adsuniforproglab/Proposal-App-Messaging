package com.leonardo.propostaapp.repository;

import java.util.List;

import com.leonardo.propostaapp.entity.Proposal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for accessing and manipulating Proposal entities.
 */
@Repository
public interface ProposalRepository extends ListCrudRepository<Proposal, Long> {

    /**
     * Finds all proposals that are not integrated with external systems.
     *
     * @return List of non-integrated proposals
     */
    List<Proposal> findAllByIntegratedIsFalse();

    /**
     * Finds all proposals that are integrated with external systems.
     *
     * @return List of integrated proposals
     */
    List<Proposal> findAllByIntegratedIsTrue();

    /**
     * Finds proposals by approval status.
     *
     * @param approved The approval status to filter by
     * @return List of proposals with the specified approval status
     */
    List<Proposal> findAllByApproved(Boolean approved);

    /**
     * Updates the integration status of a proposal.
     *
     * @param id The proposal ID
     * @param integrated The new integration status
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE proposal SET integrated = :integrated WHERE id = :id", nativeQuery = true)
    void updateIntegrationStatus(@Param("id") Long id, @Param("integrated") boolean integrated);

    /**
     * Updates the approval status and observation of a proposal.
     *
     * @param id The proposal ID
     * @param approved The approval status
     * @param observation The observation text
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE proposal SET approved = :approved, observation = :observation WHERE id = :id",
            nativeQuery = true)
    void updateApprovalStatus(
            @Param("id") Long id,
            @Param("approved") boolean approved,
            @Param("observation") String observation);
}
