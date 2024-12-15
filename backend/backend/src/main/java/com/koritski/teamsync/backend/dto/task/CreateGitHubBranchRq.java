package com.koritski.teamsync.backend.dto.task;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateGitHubBranchRq {
    private String repoFullName;
    private String branchName;
    private String ref;
    private Long taskId;
}
