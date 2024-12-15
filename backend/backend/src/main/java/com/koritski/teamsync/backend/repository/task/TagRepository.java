package com.koritski.teamsync.backend.repository.task;

import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.task.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByOrganization(Organization organization);
}
