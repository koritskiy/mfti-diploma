package com.koritski.teamsync.backend.repository.task;

import com.koritski.teamsync.backend.entity.task.TaskBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long> {
}
