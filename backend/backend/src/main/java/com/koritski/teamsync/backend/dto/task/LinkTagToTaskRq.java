package com.koritski.teamsync.backend.dto.task;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LinkTagToTaskRq {
    private Long taskId;
    private Long tagId;
}
