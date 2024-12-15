package com.koritski.teamsync.hiring.repository;

import com.koritski.teamsync.hiring.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
}