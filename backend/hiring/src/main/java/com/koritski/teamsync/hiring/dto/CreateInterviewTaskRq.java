package com.koritski.teamsync.hiring.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateInterviewTaskRq {
    private String name;
    private String question;
    private String answer;
    private String author;
    private Long taskCategoryId;
}
