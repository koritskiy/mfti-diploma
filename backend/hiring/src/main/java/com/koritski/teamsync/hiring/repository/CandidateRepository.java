package com.koritski.teamsync.hiring.repository;

import com.koritski.teamsync.hiring.entity.Candidate;
import com.koritski.teamsync.hiring.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmailAndOrganizationId(String email, Long organizationId);
    List<Candidate> findAllByOrganizationId(Long organizationId);
}
