package com.koritski.teamsync.hiring.controller;

import com.koritski.teamsync.hiring.dto.CreateInterviewResultRq;
import com.koritski.teamsync.hiring.dto.CreateInterviewRq;
import com.koritski.teamsync.hiring.dto.CreateInterviewTaskStackRq;
import com.koritski.teamsync.hiring.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/hiring/v1/interview", produces = APPLICATION_JSON_VALUE)
public class InterviewController {
    private final InterviewService interviewService;

    @PostMapping("create")
    public ResponseEntity<?> createInterview(@RequestBody CreateInterviewRq rq) {
        return interviewService.createInterview(rq);
    }

    @PutMapping("update/{interviewId}")
    public ResponseEntity<?> updateInterview(@RequestBody CreateInterviewRq rq,
                                             @PathVariable Long interviewId) {
        return interviewService.updateInterview(rq, interviewId);
    }

    @PutMapping("result/{interviewId}")
    public ResponseEntity<?> resultForInterview(@RequestBody CreateInterviewResultRq rq,
                                                @PathVariable Long interviewId) {
        return interviewService.resultForInterview(rq, interviewId);
    }

    @PostMapping("task/add")
    public ResponseEntity<?> addTaskForInterview(@RequestBody CreateInterviewTaskStackRq rq) {
        return interviewService.addTaskToInterview(rq);
    }
}
