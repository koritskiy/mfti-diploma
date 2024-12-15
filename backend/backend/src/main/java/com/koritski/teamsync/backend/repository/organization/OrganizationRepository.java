package com.koritski.teamsync.backend.repository.organization;

import com.koritski.teamsync.backend.entity.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository <Organization, Long> {
    List<Organization> findAllByOwnerUserId(Long ownerUserId);
}
