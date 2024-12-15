package com.koritski.teamsync.hiring.controller;

import com.koritski.teamsync.hiring.dto.CreateInterviewTaskRq;
import com.koritski.teamsync.hiring.service.InterviewTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.hiring.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/hiring/v1/interviewTask", produces = APPLICATION_JSON_VALUE)
public class InterviewTaskController {
    private final InterviewTaskService interviewTaskService;

    @PostMapping("create")
    public ResponseEntity<?> createInterviewTask(@RequestBody CreateInterviewTaskRq rq) {
        return interviewTaskService.createInterviewTask(rq);
    }

    @PutMapping("update/{interviewTaskId}")
    public ResponseEntity<?> updateInterviewTask(@RequestBody CreateInterviewTaskRq rq,
                                                 @PathVariable Long interviewTaskId) {
        return interviewTaskService.updateInterviewTask(rq, interviewTaskId);
    }

    @GetMapping("get/category/{categoryId}")
    public ResponseEntity<?> getAllTasksByCategory(@PathVariable Long categoryId,
                                                   @RequestHeader(AUTH_HEADER) String token) {
        return interviewTaskService.findAllTasksByCategory(categoryId, token);
    }
}
