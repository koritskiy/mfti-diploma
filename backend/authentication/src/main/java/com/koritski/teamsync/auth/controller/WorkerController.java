package com.koritski.teamsync.auth.controller;

import com.koritski.teamsync.auth.dto.rq.RqCreateWorker;
import com.koritski.teamsync.auth.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/auth/v1", produces = APPLICATION_JSON_VALUE)
public class WorkerController {
    private final WorkerService workerService;

    @PostMapping("worker/create")
    public ResponseEntity<?> createWorker(@RequestBody RqCreateWorker rq) {
        return workerService.createWorker(rq);
    }
}
