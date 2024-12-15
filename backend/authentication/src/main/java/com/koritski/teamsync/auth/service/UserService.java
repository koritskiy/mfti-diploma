package com.koritski.teamsync.auth.service;

import com.koritski.teamsync.auth.dto.rq.RqEditUser;
import com.koritski.teamsync.auth.dto.rq.RqUploadGitHubToken;
import com.koritski.teamsync.auth.dto.rs.RsTgUserData;
import com.koritski.teamsync.auth.entity.User;
import com.koritski.teamsync.auth.repository.UserRepository;
import com.koritski.teamsync.auth.util.JwtUtil;
import com.koritski.teamsync.auth.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.koritski.teamsync.auth.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получение пользователя по его id
     *
     * @param id    id-пользователя
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getUserById(Long id, String token) {
        Long userIdFromToken = jwtUtil.getUserIdFromToken(token);

        if (!Objects.equals(id, userIdFromToken)) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    "You don't have permission to do this. Please, try again later."
            );
        }

        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id = %s not found.", id)
            );
        }

        return Response.send(
                HttpStatus.OK,
                user.get()
        );
    }

    /**
     * Загрузка токена авторизации от GitHub
     *
     * @param rq    запрос с токеном авторизации
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> uploadGitHubToken(RqUploadGitHubToken rq, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<User> user = repository.findById(userId);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id = %s not found.", userId)
            );
        }

        user.get().setGitHubToken(rq.getToken());
        User updatedUser = repository.save(user.get());

        // todo: отправлять ли юзера?
        return Response.send(
                HttpStatus.OK,
                updatedUser
        );
    }

    /**
     * Подтверждение почты для пользователя
     *
     * @param secretCode код подтверждения из почты пользователя
     * @param token      токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> approveEmailForUser(String secretCode, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<User> user = repository.findById(userId);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id = %s not found.", userId)
            );
        }

        if (!Objects.equals(user.get().getSecretCode(), secretCode)) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Secret code = %s is incorrect.", secretCode)
            );
        }

        user.get().setIsEmailApproved(true);
        User updatedUser = repository.save(user.get());

        return Response.send(
                HttpStatus.OK,
                updatedUser
        );
    }

    public ResponseEntity<?> getUserTgDataById(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id = %s not found.", userId)
            );
        }

        RsTgUserData rs = new RsTgUserData()
                .setTgChatId(user.get().getTgChatId())
                .setTgUsername(user.get().getTgUsername());

        return Response.send(
                HttpStatus.OK,
                rs
        );
    }

    public ResponseEntity<?> editUserData(RqEditUser rq, String token) {
        Long userIdFromToken = jwtUtil.getUserIdFromToken(token);
        if (!Objects.equals(rq.getUserId(), userIdFromToken)) {
            return Response.send(HttpStatus.BAD_REQUEST, PERMISSION_DESCRIPTION);
        }

        Optional<User> user = repository.findById(userIdFromToken);
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id = %s not found.", rq.getUserId())
            );
        }

        if (!Objects.equals(rq.getFirstName(), "") && rq.getFirstName() != null) {
            user.get().setFirstName(rq.getFirstName());
        }
        if (!Objects.equals(rq.getLastName(), "") && rq.getLastName() != null) {
            user.get().setLastName(rq.getLastName());
        }
        if (!Objects.equals(rq.getPassword(), "")) {
            user.get().setPassword(passwordEncoder.encode(rq.getPassword()));
        }

        User updatedUser = repository.save(user.get());
        return Response.send(
                HttpStatus.OK,
                updatedUser
        );
    }
}
