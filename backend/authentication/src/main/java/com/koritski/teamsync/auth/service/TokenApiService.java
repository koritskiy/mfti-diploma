package com.koritski.teamsync.auth.service;

import com.koritski.teamsync.auth.dto.rs.RsTokenForBot;
import com.koritski.teamsync.auth.entity.User;
import com.koritski.teamsync.auth.repository.UserRepository;
import com.koritski.teamsync.auth.util.JwtUtil;
import com.koritski.teamsync.auth.util.Response;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.koritski.teamsync.auth.constants.AuthConstants.EMAIL_CLAIMS_KEY;
import static com.koritski.teamsync.auth.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenApiService {
    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    /**
     * Метод генерации токена авторизации для телеграм-бота
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> generateAuthTokenForBot(String token) {
        Claims claims = jwtUtil.getClaims(token);
        String email = claims.get(EMAIL_CLAIMS_KEY, String.class);

        Optional<User> user = repository.findUserByEmail(email);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        String tokenForBot = jwtUtil.generateTokenWithoutExpirationDate(claims);

        RsTokenForBot rs = new RsTokenForBot().setAuthToken(tokenForBot);
        return Response.send(
                HttpStatus.OK,
                rs
        );
    }
}
