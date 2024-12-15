package com.koritski.teamsync.hiring.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MoveCandidateToWorkerRq {
    private String email;
    private String name;
    private String surname;
    private Long organizationId;
}
