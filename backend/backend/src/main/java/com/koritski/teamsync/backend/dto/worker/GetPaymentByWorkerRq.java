package com.koritski.teamsync.backend.dto.worker;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetPaymentByWorkerRq {
    private Long organizationId;
    private Long workerId;
    private String reason;
}
