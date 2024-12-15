package com.koritski.teamsync.backend.repository.task;

import com.koritski.teamsync.backend.entity.task.BoardColumn;
import com.koritski.teamsync.backend.entity.task.Task;
import com.koritski.teamsync.backend.entity.organization.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByWorker(Worker worker);
}
