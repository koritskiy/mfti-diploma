package com.koritski.teamsync.auth.service;

import com.koritski.teamsync.auth.dto.rq.RqCreateWorker;
import com.koritski.teamsync.auth.dto.rs.RsAuthUser;
import com.koritski.teamsync.auth.entity.Role;
import com.koritski.teamsync.auth.entity.RoleUser;
import com.koritski.teamsync.auth.entity.User;
import com.koritski.teamsync.auth.repository.RoleRepository;
import com.koritski.teamsync.auth.repository.RoleUserRepository;
import com.koritski.teamsync.auth.repository.UserRepository;
import com.koritski.teamsync.auth.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.koritski.teamsync.auth.service.RegistrationService.getNumericString;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService {
    private final UserRepository userRepository;
    private final RoleUserRepository roleUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final static String RAW_PASSWORD = "12341234";

    /**
     * Метод создания профиля пользователя для нового сотрудника организации
     * @param rq запрос с данными о новом сотруднике
     * @return http-ответ
     */
    public ResponseEntity<?> createWorker(RqCreateWorker rq) {
        Optional<User> existingUser = userRepository.findUserByEmail(rq.getEmail());
        if (existingUser.isPresent()) {
            RsAuthUser payload = new RsAuthUser()
                    .setId(existingUser.get().getId())
                    .setFirstName(existingUser.get().getFirstName())
                    .setLastName(existingUser.get().getLastName());
            return Response.send(
                    HttpStatus.ALREADY_REPORTED,
                    payload
            );
        }

        try {
            String textEmailMessage = String.format(
                    "Добрый день, %s %s! Ваш работодатель пригласил Вас в организацию. Ваш пароль для входа: %s. Пожалуйста, поменяйте его сразу после первого входа в систему.",
                    rq.getName(), rq.getSurname(), RAW_PASSWORD
            );
            emailService.sendSimpleMessage(
                    rq.getEmail(),
                    "Добро пожаловать в Teamsync!",
                    textEmailMessage
            );
        } catch (MailParseException | MailAuthenticationException | MailSendException e) {
            log.error(e.getMessage());
        }

        User user = new User()
                .setEmail(rq.getEmail())
                .setFirstName(rq.getName())
                .setLastName(rq.getSurname())
                .setPassword(passwordEncoder.encode(RAW_PASSWORD))
                .setSecretCode(getNumericString(6))
                .setIsEmailApproved(false);
        user = userRepository.save(user);

        Optional<Role> userRole = roleRepository.findByName("USER");
        roleUserRepository.save(new RoleUser().setUser(user).setRole(userRole.get()));

        RsAuthUser payload = new RsAuthUser()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName());

        return Response.send(HttpStatus.CREATED, payload);
    }
}
