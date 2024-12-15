package com.koritski.teamsync.auth.service;

import com.koritski.teamsync.auth.constants.AuthConstants;
import com.koritski.teamsync.auth.dto.rq.RqForgotPassword;
import com.koritski.teamsync.auth.dto.rq.RqSignIn;
import com.koritski.teamsync.auth.dto.rs.RsAuthUser;
import com.koritski.teamsync.auth.entity.RoleEnum;
import com.koritski.teamsync.auth.entity.RoleUser;
import com.koritski.teamsync.auth.entity.User;
import com.koritski.teamsync.auth.repository.RoleUserRepository;
import com.koritski.teamsync.auth.repository.UserRepository;
import com.koritski.teamsync.auth.util.JwtUtil;
import com.koritski.teamsync.auth.util.Response;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.koritski.teamsync.auth.constants.AuthConstants.IS_ADMIN_ROLE_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final RoleUserRepository roleUserRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Метод авторизации пользователя
     * @param rq запрос с данными о пользователе (связка почта + пароль)
     * @return http-ответ
     */
    public ResponseEntity<?> signIn(RqSignIn rq) {
        Optional<User> user = userRepository.findUserByEmail(rq.getEmail());
        if (user.isPresent() && passwordEncoder.matches(rq.getPassword(), user.get().getPassword())) {
            Claims claims = getRoleClaimsForUser(user.get());
            claims.put(AuthConstants.ID_CLAIMS_KEY, user.get().getId());
            claims.put(AuthConstants.EMAIL_CLAIMS_KEY, rq.getEmail());

            String token = jwtUtil.generateToken(claims);

            RsAuthUser payload = new RsAuthUser()
                    .setId(user.get().getId())
                    .setFirstName(user.get().getFirstName())
                    .setLastName(user.get().getLastName())
                    .setIsUserAdmin(claims.get(IS_ADMIN_ROLE_EXIST, Boolean.class))
                    .setToken(token);

            if (user.get().getGitHubToken() != null) {
                payload.setGitHubToken(user.get().getGitHubToken());
            }
            return Response.send(HttpStatus.CREATED, "Login by email ended successfully.", payload);
        }
        return Response.send(HttpStatus.BAD_REQUEST, "Incorrect data: user with email does not exist or password mismatch.");
    }

    /**
     * Метод восстановления пароля для пользователя. Метод сработает только при подтвержденной почте пользователя
     * @param rq запрос с почтой пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> forgotPassword(RqForgotPassword rq) {
        Optional<User> user = userRepository.findUserByEmail(rq.getEmail());
        if (!user.isPresent()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("User with email = %s not found.", rq.getEmail())
            );
        }

        if (!user.get().getIsEmailApproved()) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    "Email is not approved"
            );
        }

        String newPassword = getAlphaNumericString(8);
        try {
            String textEmailMessage = String.format(
                    "Добрый день! На связи Teamsync! Ваш новый пароль: %s",
                    newPassword
            );
            emailService.sendSimpleMessage(
                    rq.getEmail(),
                    "Ваш новый пароль от Teamsync",
                    textEmailMessage
            );
        } catch (MailParseException | MailAuthenticationException | MailSendException e) {
            log.error(e.getMessage());
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
        return Response.send(
                HttpStatus.OK,
                "Password was change successfully."
        );
    }

    /**
     * Метод генерации нового пароля для пользователя
     * @param n длина пароля
     * @return сгенерированный пароль длиной n
     */
    private static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    /**
     * Получение claims для определенного пользователя. Claims нужны для корректной генерации токена пользователя
     * @param user объект пользователя
     * @return сгенерированные claims
     */
    public Claims getRoleClaimsForUser(User user) {
        Claims claims = Jwts.claims();
        claims.put(AuthConstants.IS_USER_ROLE_EXIST, false);
        claims.put(IS_ADMIN_ROLE_EXIST, false);
        claims.put(AuthConstants.IS_MODERATOR_ROLE_EXIST, false);

        List<RoleUser> roles = roleUserRepository.findAllByUser(user);
        for (RoleUser role : roles) {
            if (Objects.equals(role.getRole().getId(), RoleEnum.USER.getId())) {
                claims.replace(AuthConstants.IS_USER_ROLE_EXIST, true);
            } else if (Objects.equals(role.getRole().getId(), RoleEnum.ADMIN.getId())) {
                claims.replace(IS_ADMIN_ROLE_EXIST, true);
            } else if (Objects.equals(role.getRole().getId(), RoleEnum.MODERATOR.getId())) {
                claims.replace(AuthConstants.IS_MODERATOR_ROLE_EXIST, true);
            }
        }

        return claims;
    }
}
