package com.koritski.teamsync.hiring.repository;

import com.koritski.teamsync.hiring.entity.InterviewTaskStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewTaskStackRepository extends JpaRepository<InterviewTaskStack, Long> {
}
