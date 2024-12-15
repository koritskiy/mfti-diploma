package com.koritski.teamsync.auth.controller;

import com.koritski.teamsync.auth.dto.rq.RqForgotPassword;
import com.koritski.teamsync.auth.dto.rq.RqSignIn;
import com.koritski.teamsync.auth.dto.rq.RqSignUp;
import com.koritski.teamsync.auth.service.LoginService;
import com.koritski.teamsync.auth.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/auth/v1", produces = APPLICATION_JSON_VALUE)
public class AuthController {
    private final RegistrationService registrationService;
    private final LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RqSignUp rq) {
        return registrationService.signUp(rq);
    }

    @PostMapping("/login/email")
    public ResponseEntity<?> signIn(@RequestBody RqSignIn rq) {
        return loginService.signIn(rq);
    }

    @PutMapping("forgot/password")
    public ResponseEntity<?> forgotPassword(@RequestBody RqForgotPassword rq) {
        return loginService.forgotPassword(rq);
    }
}
