package com.koritski.teamsync.hiring.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateInterviewResultRq {
    private String interviewFeedback;
    private String interviewDecision;
}
