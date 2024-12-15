package com.koritski.teamsync.backend.repository.task;

import com.koritski.teamsync.backend.entity.task.Tag;
import com.koritski.teamsync.backend.entity.task.Task;
import com.koritski.teamsync.backend.entity.task.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    List<TaskTag> findAllByTask(Task task);
    List<TaskTag> findAllByTag(Tag tag);
    void deleteAllByTag(Tag tag);
}
