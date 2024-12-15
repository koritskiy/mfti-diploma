package com.koritski.teamsync.hiring.dto;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class CreateInterviewRq {
    private String recruiter;
    private Long candidateId;
}
