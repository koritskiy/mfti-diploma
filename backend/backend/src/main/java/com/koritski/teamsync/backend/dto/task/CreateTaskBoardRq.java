package com.koritski.teamsync.backend.dto.task;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTaskBoardRq {
    private String name;
    private Long organizationId;
}
