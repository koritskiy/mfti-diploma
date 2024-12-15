package com.koritski.teamsync.backend.repository.organization;

import com.koritski.teamsync.backend.entity.organization.Function;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.entity.organization.WorkerFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerFunctionRepository extends JpaRepository <WorkerFunction, Long> {
    Optional<WorkerFunction> findByFunctionAndWorker(Function function, Worker worker);
}
