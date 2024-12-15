package com.koritski.teamsync.backend.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddFunctionForWorkerRq {
    private Long functionId;
    private Long workerId;
    private Long tariffRate;
}
