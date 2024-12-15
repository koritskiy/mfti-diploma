package com.koritski.teamsync.hiring.repository;

import com.koritski.teamsync.hiring.entity.InterviewTask;
import com.koritski.teamsync.hiring.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewTaskRepository extends JpaRepository<InterviewTask, Long> {
    List<InterviewTask> findAllByTaskCategory(TaskCategory taskCategory);
}