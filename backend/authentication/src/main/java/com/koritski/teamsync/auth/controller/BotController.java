package com.koritski.teamsync.auth.controller;

import com.koritski.teamsync.auth.dto.rq.RqFillTelegramData;
import com.koritski.teamsync.auth.exception.JwtTokenMalformedException;
import com.koritski.teamsync.auth.exception.JwtTokenMissingException;
import com.koritski.teamsync.auth.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.auth.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/auth/v1/bot", produces = APPLICATION_JSON_VALUE)
public class BotController {
    private final BotService service;

    @GetMapping("validate/user")
    public ResponseEntity<?> validateUserToken(@RequestHeader(AUTH_HEADER) String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        return service.validateTokenFromBot(token);
    }

    @PostMapping("save/tg/data")
    public ResponseEntity<?> saveUserTelegramData(@RequestBody RqFillTelegramData rq,
                                                  @RequestHeader(AUTH_HEADER) String token) {
        return service.saveUserTelegramData(rq, token);
    }
}
