package com.koritski.teamsync.backend.repository.task;

import com.koritski.teamsync.backend.entity.task.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
}
