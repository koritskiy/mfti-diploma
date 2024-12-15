package com.koritski.teamsync.auth.controller;

import com.koritski.teamsync.auth.service.TokenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.auth.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/auth/v1/token", produces = APPLICATION_JSON_VALUE)
public class TokenApiController {
    private final TokenApiService service;

    /**
     * Это API для получения пользователем authToken для авторизации в приложении через бот
     * @param token токен-пользователя
     * @return возвращается authToken для бота
     */
    @PostMapping("create")
    public ResponseEntity<?> generateAuthToken(@RequestHeader(AUTH_HEADER) String token) {
        return service.generateAuthTokenForBot(token);
    }
}
