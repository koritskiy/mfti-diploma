package com.koritski.teamsync.hiring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.koritski.teamsync.hiring.dto.CreateCandidateRq;
import com.koritski.teamsync.hiring.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.hiring.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/hiring/v1/candidate", produces = APPLICATION_JSON_VALUE)
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("create")
    public ResponseEntity<?> createCandidate(@RequestBody CreateCandidateRq rq,
                                             @RequestHeader(AUTH_HEADER) String token) {
        return candidateService.createCandidate(rq);
    }

    @PutMapping("update/{candidateId}")
    public ResponseEntity<?> updateInterviewTask(@RequestBody CreateCandidateRq rq,
                                                 @PathVariable Long candidateId) {
        return candidateService.updateCandidate(rq, candidateId);
    }

    @GetMapping("get/{organizationId}")
    public ResponseEntity<?> getAllCandidates(@PathVariable Long organizationId,
                                              @RequestHeader(AUTH_HEADER) String token) {
        return candidateService.getAllCandidates(organizationId, token);
    }

    @GetMapping("{candidateId}")
    public ResponseEntity<?> getCandidateById(@PathVariable Long candidateId,
                                              @RequestHeader(AUTH_HEADER) String token) {
        return candidateService.getCandidateById(candidateId, token);
    }

    @GetMapping("stats/{organizationId}")
    public ResponseEntity<?> getStatsForHiring(@PathVariable Long organizationId,
                                               @RequestHeader(AUTH_HEADER) String token) {
        return candidateService.getStatsForHiring(organizationId, token);
    }

    @PutMapping("move/worker/{candidateId}")
    public ResponseEntity<?> moveCandidateToWorker(@PathVariable Long candidateId,
                                                   @RequestHeader(AUTH_HEADER) String token) throws JsonProcessingException {
        return candidateService.moveCandidateToWorker(candidateId, token);
    }
}
