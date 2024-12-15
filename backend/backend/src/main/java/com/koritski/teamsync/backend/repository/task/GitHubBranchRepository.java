package com.koritski.teamsync.backend.repository.task;

import com.koritski.teamsync.backend.entity.task.GitHubBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitHubBranchRepository extends JpaRepository<GitHubBranch, Long> {
}
