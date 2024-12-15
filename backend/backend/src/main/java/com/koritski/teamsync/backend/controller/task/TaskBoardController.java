package com.koritski.teamsync.backend.controller.task;


import com.koritski.teamsync.backend.dto.task.CreateTaskBoardRq;
import com.koritski.teamsync.backend.service.task.TaskBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/taskBoard", produces = APPLICATION_JSON_VALUE)
public class TaskBoardController {
    private final TaskBoardService service;

    @PostMapping("create")
    public ResponseEntity<?> createTaskBoard(@RequestBody CreateTaskBoardRq rq,
                                             @RequestHeader(AUTH_HEADER) String token) {

        return service.createTaskBoard(rq, token);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getTaskBoardById(@PathVariable Long id,
                                              @RequestHeader(AUTH_HEADER) String token) {
        return service.getTaskBoardById(id, token);
    }
}
