package com.koritski.teamsync.backend.repository.organization;

import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository <Worker, Long>{
    Optional<Worker> findByUserId(Long userId);
    Optional<Worker> findByUserIdAndOrganization(Long userId, Organization organization);
    List<Worker> findAllByOrganization(Organization organization);
}
