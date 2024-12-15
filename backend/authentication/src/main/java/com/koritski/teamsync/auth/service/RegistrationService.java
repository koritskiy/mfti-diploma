package com.koritski.teamsync.auth.service;

import com.koritski.teamsync.auth.constants.AuthConstants;
import com.koritski.teamsync.auth.dto.rq.RqSignUp;
import com.koritski.teamsync.auth.dto.rs.RsAuthUser;
import com.koritski.teamsync.auth.entity.Role;
import com.koritski.teamsync.auth.entity.RoleUser;
import com.koritski.teamsync.auth.entity.User;
import com.koritski.teamsync.auth.repository.RoleRepository;
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

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Метод регистрации пользователя
     * @param rq запрос с данными о новом пользователе
     * @return http-ответ
     */
    @Transactional
    public ResponseEntity<?> signUp(RqSignUp rq) {
        if (userRepository.existsByEmail(rq.getEmail())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("User with email = %s is already exist.", rq.getEmail())
            );
        }

        String codeForApprovingEmail = getNumericString(6);
        User user = new User()
                .setEmail(rq.getEmail())
                .setFirstName(rq.getFirstName())
                .setLastName(rq.getLastName())
                .setPassword(passwordEncoder.encode(rq.getPassword()))
                .setSecretCode(codeForApprovingEmail)
                .setIsEmailApproved(false);
        user = userRepository.save(user);

        Optional<Role> userRole = roleRepository.findByName("USER");
        roleUserRepository.save(new RoleUser().setUser(user).setRole(userRole.get()));

        Claims claims = Jwts.claims();
        claims.put(AuthConstants.ID_CLAIMS_KEY, user.getId());
        claims.put(AuthConstants.EMAIL_CLAIMS_KEY, user.getEmail());
        claims.put(AuthConstants.IS_USER_ROLE_EXIST, true);
        claims.put(AuthConstants.IS_MODERATOR_ROLE_EXIST, false);
        claims.put(AuthConstants.IS_ADMIN_ROLE_EXIST, false);
        String token = jwtUtil.generateToken(claims);

        try {
            String textEmailMessage = String.format(
                    "Добрый день, %s %s! Добро пожаловать в Teamsync! Для подтверждения почты введите в личном кабинете код подтверждения: %s",
                    rq.getFirstName(), rq.getLastName(), codeForApprovingEmail
            );
            emailService.sendSimpleMessage(
                    rq.getEmail(),
                    "Добро пожаловать в Teamsync!",
                    textEmailMessage
            );
        } catch (MailParseException | MailAuthenticationException | MailSendException e) {
            log.error(e.getMessage());
        }

        RsAuthUser payload = new RsAuthUser()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setToken(token)
                .setIsUserAdmin(false);

        return Response.send(
                HttpStatus.CREATED,
                payload
        );
    }

    /**
     * Генерация кода подтверждения почты
     * @param n длина кода
     * @return случайная строка из цифр длины n
     */
    public static String getNumericString(int n) {
        String AlphaNumericString = "0123456789";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
