package com.koritski.teamsync.backend.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DismissWorkerRq {
    private Long organizationId;
    private Long workerId;
}
