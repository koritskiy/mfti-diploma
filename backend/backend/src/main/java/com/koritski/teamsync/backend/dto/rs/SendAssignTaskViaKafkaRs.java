package com.koritski.teamsync.backend.dto.rs;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SendAssignTaskViaKafkaRs {
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private Long userTgChatId;
}
