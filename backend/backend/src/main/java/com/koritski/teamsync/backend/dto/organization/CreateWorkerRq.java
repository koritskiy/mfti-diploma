package com.koritski.teamsync.backend.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateWorkerRq {
    private String email;
    private String name;
    private String surname;
    private Long organizationId;
}
