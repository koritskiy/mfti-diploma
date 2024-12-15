package com.koritski.teamsync.auth.service;

import com.koritski.teamsync.auth.dto.rq.RqFillTelegramData;
import com.koritski.teamsync.auth.entity.User;
import com.koritski.teamsync.auth.exception.JwtTokenMalformedException;
import com.koritski.teamsync.auth.exception.JwtTokenMissingException;
import com.koritski.teamsync.auth.repository.UserRepository;
import com.koritski.teamsync.auth.util.JwtUtil;
import com.koritski.teamsync.auth.util.Response;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.koritski.teamsync.auth.constants.AuthConstants.EMAIL_CLAIMS_KEY;
import static com.koritski.teamsync.auth.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Service
@RequiredArgsConstructor
public class BotService {
    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    /**
     * Метод проверки токена авторизации, который пользователь отправил в телеграм-бот
     * @param botToken отправленный токен пользователя
     * @return http-ответ
     * @throws JwtTokenMalformedException некорректный токен
     * @throws JwtTokenMissingException токен вовсе не отправлен
     */
    public ResponseEntity<?> validateTokenFromBot(String botToken) throws JwtTokenMalformedException, JwtTokenMissingException {
        jwtUtil.validateToken(botToken);

        Claims claims = jwtUtil.getClaims(botToken);
        String email = claims.get(EMAIL_CLAIMS_KEY, String.class);

        Optional<User> user = repository.findUserByEmail(email);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        return Response.send(
                HttpStatus.OK,
                user.get()
        );
    }

    /**
     * Сохранение данных пользователя из его телеграм-аккаунта
     * @param rq запрос с данными из телеграмма
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> saveUserTelegramData(RqFillTelegramData rq, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);

        Optional<User> user = repository.findById(userId);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id = %s not found.", userId)
            );
        }

        user.get()
                .setTgFirstName(rq.getFirstName())
                .setTgLastName(rq.getFirstName())
                .setTgChatId(rq.getChatId())
                .setTgUsername(rq.getUsername());

        User updatedUser = repository.save(user.get());

        return Response.send(
                HttpStatus.OK,
                updatedUser
        );
    }
}
