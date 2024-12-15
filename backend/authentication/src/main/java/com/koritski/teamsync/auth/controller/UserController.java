package com.koritski.teamsync.auth.controller;

import com.koritski.teamsync.auth.dto.rq.RqEditUser;
import com.koritski.teamsync.auth.dto.rq.RqUploadGitHubToken;
import com.koritski.teamsync.auth.service.UserService;
import com.koritski.teamsync.auth.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.auth.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/auth/v1/user", produces = APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService service;

    @GetMapping("get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
                                         @RequestHeader(AUTH_HEADER) String token) {
        return service.getUserById(id, token);
    }

    @PutMapping("upload/token/github")
    public ResponseEntity<?> uploadGitHubToken(@RequestBody RqUploadGitHubToken rq,
                                               @RequestHeader(AUTH_HEADER) String token) {
        return service.uploadGitHubToken(rq, token);
    }

    @PutMapping("approve/email/{secretCode}")
    public ResponseEntity<?> approveEmail(@PathVariable String secretCode,
                                          @RequestHeader(AUTH_HEADER) String token) {
        return service.approveEmailForUser(secretCode, token);
    }

    @GetMapping("get/tg/{id}")
    public ResponseEntity<?> getUserTgDataById(@PathVariable Long id) {
        return service.getUserTgDataById(id);
    }

    @PutMapping("edit")
    public ResponseEntity<?> editUserData(@RequestBody RqEditUser rq, @RequestHeader(AUTH_HEADER) String token) {
        return service.editUserData(rq, token);
    }
}
