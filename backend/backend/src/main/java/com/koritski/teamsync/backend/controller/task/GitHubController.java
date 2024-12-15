package com.koritski.teamsync.backend.controller.task;

import com.koritski.teamsync.backend.dto.task.CreateGitHubBranchRq;
import com.koritski.teamsync.backend.service.task.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/git", produces = APPLICATION_JSON_VALUE)
public class GitHubController {
    private final GitHubService service;

    @PostMapping("branch/create")
    public ResponseEntity<?> createGitHubBranch(@RequestBody CreateGitHubBranchRq rq,
                                                @RequestHeader(AUTH_HEADER) String token) {
        return service.createGitHubBranch(rq, token);
    }
}
