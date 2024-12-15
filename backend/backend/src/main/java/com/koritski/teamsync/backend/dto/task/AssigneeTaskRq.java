package com.koritski.teamsync.backend.dto.task;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AssigneeTaskRq {
    private Long workerId;
    private Long taskId;
}
