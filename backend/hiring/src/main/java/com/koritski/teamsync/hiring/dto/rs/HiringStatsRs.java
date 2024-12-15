package com.koritski.teamsync.hiring.dto.rs;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HiringStatsRs {
    private Long countCandidates;
    private Long countCandidatesWithoutInterview;
    private Long countInterviews;
    private Long countTaskCategories;
    private Long countTasks;
}
