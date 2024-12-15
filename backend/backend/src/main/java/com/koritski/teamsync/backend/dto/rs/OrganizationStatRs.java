package com.koritski.teamsync.backend.dto.rs;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrganizationStatRs {
    private Long countActualWorkers;
    private Long countFiredWorkers;
    private Long countTaskBoards;
    private Long countTasks;
    private Long totalSalary;
}
