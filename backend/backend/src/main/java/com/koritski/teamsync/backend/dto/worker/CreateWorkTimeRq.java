package com.koritski.teamsync.backend.dto.worker;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
public class CreateWorkTimeRq {
    private Long userId;
    private Long workerFunctionId;
}
