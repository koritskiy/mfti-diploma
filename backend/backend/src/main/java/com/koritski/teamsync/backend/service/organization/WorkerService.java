package com.koritski.teamsync.backend.service.organization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koritski.teamsync.backend.dto.organization.*;
import com.koritski.teamsync.backend.dto.organization.DismissWorkerRq;
import com.koritski.teamsync.backend.dto.worker.FillWorkerPayInfoRq;
import com.koritski.teamsync.backend.entity.WorkerStatusEnum;
import com.koritski.teamsync.backend.entity.organization.Function;
import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.WorkerFunction;
import com.koritski.teamsync.backend.repository.organization.FunctionRepository;
import com.koritski.teamsync.backend.repository.organization.OrganizationRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerFunctionRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.util.JwtUtil;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.koritski.teamsync.backend.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final OrganizationRepository organizationRepository;
    private final FunctionRepository functionRepository;
    private final WorkerFunctionRepository workerFunctionRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    /**
     * Метод создания сотрудника
     * @param rq запрос с данными сотрудниками
     * @param token токен пользователя
     * @return http-ответ
     * @throws JsonProcessingException во время запроса в auth может прийти некорректный json
     */
    public ResponseEntity<?> createWorker(CreateWorkerRq rq, String token) throws JsonProcessingException {
        Optional<Organization> organization = organizationRepository.findById(rq.getOrganizationId());
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", rq.getOrganizationId())
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

        CreateWorkerInAuthRq requestForAuth = new CreateWorkerInAuthRq()
                .setEmail(rq.getEmail())
                .setName(rq.getName())
                .setSurname(rq.getSurname());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpEntity<?> request = new HttpEntity<>(requestForAuth, headers);

        Worker worker;
        try {
            // todo вынести строку в проперти
            String jsonResponseString = restTemplate.postForObject(
                    "http://localhost:8082/api/auth/v1/worker/create",
                    request,
                    String.class
            );

            JsonNode root = objectMapper.readTree(jsonResponseString);
            if (root.path("status").asInt() == 400) {
                return Response.send(
                        HttpStatus.BAD_REQUEST,
                        root.path("description").asText()
                );
            }

            Long workerId = root.path("payload").path("id").asLong();

            if (root.path("status").asInt() == 201) {
                worker = new Worker()
                        .setEmail(rq.getEmail())
                        .setFirstName(rq.getName())
                        .setLastName(rq.getSurname())
                        .setUserId(workerId)
                        .setStatus(WorkerStatusEnum.WORK.name())
                        .setOrganization(organization.get())
                        .setIsOrganizationAdmin(false);

                worker = workerRepository.save(worker);
            } else {
                // Случай, когда пользователь уже существовал до этого и он просто добавляется в другую организацию
                Optional<Worker> existingWorker = workerRepository.findByUserId(workerId);
                if (existingWorker.isEmpty()) {
                    return Response.send(
                            HttpStatus.BAD_REQUEST,
                            "Something went wrong. Please, call to support."
                    );
                }

                existingWorker.get()
                        .setStatus(WorkerStatusEnum.WORK.name())
                        .setOrganization(organization.get())
                        .setIsOrganizationAdmin(false);
                worker = workerRepository.save(existingWorker.get());
            }
        } catch (HttpClientErrorException e) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }

        return Response.send(
                HttpStatus.CREATED,
                worker
        );
    }

    public ResponseEntity<?> fillWorkerPayInfo(Long workerId, FillWorkerPayInfoRq rq, String token) {
        Optional<Worker> worker = workerRepository.findById(workerId);
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s did not found.", workerId)
            );
        }

        if (!Objects.equals(worker.get().getUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        worker.get()
                .setMiddleName(rq.getMiddleName())
                .setInn(rq.getInn())
                .setWorkerBank(rq.getWorkerBank())
                .setBankBik(rq.getBankBik())
                .setBankAccountNumber(rq.getBankAccountNumber())
                .setAccountNumber(rq.getAccountNumber());

        Worker updatedWorker = workerRepository.save(worker.get());
        return Response.send(
                HttpStatus.OK,
                updatedWorker
        );
    }

    /**
     * Метод добавления должности для сотрудника
     * @param rq запрос с данными о должности и о сотруднике
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> addFunctionForWorker(AddFunctionForWorkerRq rq, String token) {
        Optional<Worker> worker = workerRepository.findById(rq.getWorkerId());
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s did not found.", rq.getWorkerId())
            );
        }

        Optional<Function> function = functionRepository.findById(rq.getFunctionId());
        if (function.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Function with id = %s did not found.", rq.getFunctionId())
            );
        }

        if (!Objects.equals(function.get().getOrganization().getOwnerUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        WorkerFunction workerFunction = new WorkerFunction()
                .setFunction(function.get())
                .setWorker(worker.get())
                .setTariffRate(rq.getTariffRate())
                .setFunctionName(function.get().getName());
        workerFunction = workerFunctionRepository.save(workerFunction);

        return Response.send(
                HttpStatus.CREATED,
                workerFunction.getWorker()
        );
    }

    /**
     * Метод увольнения сотрудника
     * @param rq запрос с данными о сотруднике
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> dismissWorker(DismissWorkerRq rq, String token) {
        Optional<Organization> organization = organizationRepository.findById(rq.getOrganizationId());
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", rq.getOrganizationId())
            );
        }

        Optional<Worker> worker = workerRepository.findById(rq.getWorkerId());
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s not found.", rq.getWorkerId())
            );
        }

        if (!Objects.equals(organization.get().getOwnerUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        worker.get().setStatus(WorkerStatusEnum.FIRED.name());
        Worker updatedWorker = workerRepository.save(worker.get());

        return Response.send(
                HttpStatus.OK,
                updatedWorker
        );
    }

    /**
     * Метод восстановления сотрудника после увольнения
     * @param rq запрос для восстановления сотрудника
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> rehireWorker(RehireWorkerRq rq, String token) {
        Optional<Organization> organization = organizationRepository.findById(rq.getOrganizationId());
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", rq.getOrganizationId())
            );
        }

        Optional<Worker> worker = workerRepository.findById(rq.getWorkerId());
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s not found.", rq.getWorkerId())
            );
        }

        if (!Objects.equals(organization.get().getOwnerUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        worker.get().setStatus(WorkerStatusEnum.WORK.name());
        Worker updatedWorker = workerRepository.save(worker.get());

        return Response.send(
                HttpStatus.OK,
                updatedWorker
        );
    }

    /**
     * Получение всех сотрудников организации
     * @param organizationId id-организации
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getWorkers(Long organizationId, String token) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        if (!Objects.equals(organization.get().getOwnerUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        return Response.send(
                HttpStatus.OK,
                organization.get().getWorkers()
        );
    }

    /**
     * Получение текущих сотрудников организации
     * @param organizationId id-организации
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getActualWorkers(Long organizationId, String token) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        if (!Objects.equals(organization.get().getOwnerUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        List<Worker> workers = organization.get().getWorkers()
                .stream().filter(w -> Objects.equals(w.getStatus(), WorkerStatusEnum.WORK.name()))
                .collect(Collectors.toList());

        return Response.send(
                HttpStatus.OK,
                workers
        );
    }

    /**
     * Метод получения всех уволенных сотрудников организации
     * @param organizationId id-организации
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getFiredWorkers(Long organizationId, String token) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        if (!Objects.equals(organization.get().getOwnerUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        List<Worker> workers = organization.get().getWorkers()
                .stream().filter(w -> Objects.equals(w.getStatus(), WorkerStatusEnum.FIRED.name()))
                .collect(Collectors.toList());

        return Response.send(
                HttpStatus.OK,
                workers
        );
    }

    /**
     * Метод получения сотрудника по его id
     * @param workerId id-сотрудника
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getWorkerById(Long workerId, String token) {
        Optional<Worker> worker = workerRepository.findById(workerId);
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s not found.", workerId)
            );
        }

        if (!Objects.equals(worker.get().getOrganization().getOwnerUserId(), jwtUtil.getUserIdFromToken(token))) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        return Response.send(
                HttpStatus.OK,
                worker.get()
        );
    }

    /**
     * Метод получения сотрудника по id-пользователя (из auth)
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getWorkerByUserId(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Worker> worker = workerRepository.findByUserId(userId);
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s not found", userId)
            );
        }

        return Response.send(
                HttpStatus.OK,
                worker.get()
        );
    }
}
