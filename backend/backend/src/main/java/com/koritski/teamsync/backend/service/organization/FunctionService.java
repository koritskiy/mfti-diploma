package com.koritski.teamsync.backend.service.organization;

import com.koritski.teamsync.backend.dto.organization.CreateFunctionRq;
import com.koritski.teamsync.backend.entity.organization.Function;
import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.repository.organization.FunctionRepository;
import com.koritski.teamsync.backend.repository.organization.OrganizationRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.util.JwtUtil;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.koritski.teamsync.backend.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class FunctionService {
    private final FunctionRepository repository;
    private final WorkerRepository workerRepository;
    private final OrganizationRepository organizationRepository;
    private final JwtUtil jwtUtil;

    /**
     * Метод создания должности для организации
     * @param rq запрос с данными по новой должности
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> createFunction(CreateFunctionRq rq, String token) {
        Optional<Organization> organization = organizationRepository.findById(rq.getOrganizationId());
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s did not found.", rq.getOrganizationId())
            );
        }

        Optional<Worker> currentWorker = workerRepository.findByUserIdAndOrganization(
                jwtUtil.getUserIdFromToken(token),
                organization.get()
        );
        if (currentWorker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    "Current user is not worker for organization"
            );
        }

        if (!currentWorker.get().getIsOrganizationAdmin()) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        Function function = new Function()
                .setName(rq.getName())
                .setOrganization(organization.get());

        function = repository.save(function);

        return Response.send(
                HttpStatus.OK,
                function
        );
    }

    /**
     * Метод удаления должности
     * @param id id-должности
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> deleteFunction(Long id, String token) {
        Optional<Worker> currentWorker = workerRepository.findByUserId(jwtUtil.getUserIdFromToken(token));
        if (currentWorker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    "Current user is not worker for organization"
            );
        }

        if (!currentWorker.get().getIsOrganizationAdmin()) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        repository.deleteById(id);
        return Response.send(
                HttpStatus.OK,
                String.format("Function with id = %s deleted successfully.", id)
        );
    }
}
