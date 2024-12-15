package com.koritski.teamsync.backend.dto.task;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTaskRq {
    private String name;
    private String description;
    private Long boardColumnId;
}
