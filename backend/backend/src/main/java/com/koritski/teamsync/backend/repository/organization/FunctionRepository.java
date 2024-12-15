package com.koritski.teamsync.backend.repository.organization;

import com.koritski.teamsync.backend.entity.organization.Function;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FunctionRepository extends JpaRepository <Function, Long> {
}
