package com.koritski.teamsync.hiring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koritski.teamsync.hiring.dto.MoveCandidateToWorkerRq;
import com.koritski.teamsync.hiring.util.Response;
import com.koritski.teamsync.hiring.dto.CreateCandidateRq;
import com.koritski.teamsync.hiring.dto.rs.HiringStatsRs;
import com.koritski.teamsync.hiring.entity.Candidate;
import com.koritski.teamsync.hiring.entity.TaskCategory;
import com.koritski.teamsync.hiring.repository.CandidateRepository;
import com.koritski.teamsync.hiring.repository.TaskCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final TaskCategoryRepository taskCategoryRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    /**
     * Метод создания кандидата
     * @param rq запрос с данными о новом кандидате
     * @return http-ответ
     */
    public ResponseEntity<?> createCandidate(CreateCandidateRq rq) {
        // todo: Добавить проверку на существование организации
        Optional<Candidate> candidateOptional
                = candidateRepository.findByEmailAndOrganizationId(rq.getEmail(), rq.getOrganizationId());

        if (candidateOptional.isPresent()) {
            return Response.send(
                    HttpStatus.CONFLICT,
                    String.format("Candidate with email = %s already exists.", rq.getEmail())
            );
        }

        Candidate candidate = new Candidate()
                .setFirstName(rq.getFirstName())
                .setMiddleName(rq.getMiddleName())
                .setLastName(rq.getLastName())
                .setEmail(rq.getEmail())
                .setPhone(rq.getPhone())
                .setOrganizationId(rq.getOrganizationId())
                .setResumeLink(rq.getResumeLink())
                .setPosition(rq.getPosition())
                .setTariff(rq.getTariff())
                .setTelegramName(rq.getTelegramName())
                .setIsHired(false);

        candidateRepository.save(candidate);

        return Response.send(
                HttpStatus.OK,
                candidate
        );
    }

    /**
     * Метод обновления кандидата
     * @param rq новые данные кандидата
     * @param id id-кандидата
     * @return http-ответ
     */
    public ResponseEntity<?> updateCandidate(CreateCandidateRq rq, Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);

        if (candidate.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Candidate with id = %s not found.", id)
            );
        }

        // todo: Добавить проверку на существование организации

        candidate.get()
                .setFirstName(rq.getFirstName())
                .setMiddleName(rq.getMiddleName())
                .setLastName(rq.getLastName())
                .setEmail(rq.getEmail())
                .setPhone(rq.getPhone())
                .setOrganizationId(rq.getOrganizationId())
                .setResumeLink(rq.getResumeLink())
                .setPosition(rq.getPosition())
                .setTariff(rq.getTariff())
                .setTelegramName(rq.getTelegramName());

        Candidate updatedCandidate = candidateRepository.save(candidate.get());

        return Response.send(
                HttpStatus.OK,
                updatedCandidate
        );
    }

    /**
     * Получение всех кандидатов организации
     * @param organizationId id-организации
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getAllCandidates(Long organizationId, String token) {
        // todo проверка владелец организации == id из токена
        List<Candidate> candidates = candidateRepository.findAllByOrganizationId(organizationId);
        if (candidates.size() == 0) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Candidates in organization with id = %s not found.", organizationId)
            );
        }
        return Response.send(
                HttpStatus.OK,
                candidates
        );
    }

    /**
     * Получение кандидата по его id
     * @param candidateId id-кандидата
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getCandidateById(Long candidateId, String token) {
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if (candidate.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Candidate with id = %s not found", candidateId)
            );
        }

        return Response.send(
                HttpStatus.OK,
                candidate.get()
        );
    }

    /**
     * Получение статистики по сервису найма сотрудников
     * @param organizationId id-организации
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getStatsForHiring(Long organizationId, String token) {
        List<Candidate> candidates = candidateRepository.findAllByOrganizationId(organizationId);
        List<TaskCategory> taskCategories = taskCategoryRepository.findAllByOrganizationId(organizationId);

        long countInterviews = 0, countCandidatesWithoutInterview = 0;;
        for (Candidate candidate : candidates) {
            countInterviews += candidate.getInterviews().size();
            if (candidate.getInterviews().size() == 0) {
                countCandidatesWithoutInterview++;
            }
        }

        long countTasks = 0;
        for (TaskCategory taskCategory : taskCategories) {
            countTasks += taskCategory.getInterviewTasks().size();
        }

        HiringStatsRs rs = new HiringStatsRs()
                .setCountCandidates((long) candidates.size())
                .setCountCandidatesWithoutInterview(countCandidatesWithoutInterview)
                .setCountInterviews(countInterviews)
                .setCountTaskCategories((long) taskCategories.size())
                .setCountTasks(countTasks);

        return Response.send(
                HttpStatus.OK,
                rs
        );
    }

    public ResponseEntity<?> moveCandidateToWorker(Long candidateId, String token) throws JsonProcessingException {
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if (candidate.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Candidate with id = %s not found.", candidateId)
            );
        }

        MoveCandidateToWorkerRq requestForBackend = new MoveCandidateToWorkerRq()
                .setEmail(candidate.get().getEmail())
                .setName(candidate.get().getFirstName())
                .setSurname(candidate.get().getLastName())
                .setOrganizationId(candidate.get().getOrganizationId());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<?> request = new HttpEntity<>(requestForBackend, headers);

        // todo вынести строку в проперти
        String jsonResponseString = restTemplate.postForObject(
                "http://localhost:8080/api/tracker/v1/worker/create",
                request,
                String.class
        );

        JsonNode root = objectMapper.readTree(jsonResponseString);
        if (root.path("status").asInt() != 201) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    "Something went wrong."
            );
        }

        candidate.get().setIsHired(true);
        candidateRepository.save(candidate.get());

        return Response.send(
                HttpStatus.CREATED,
                "Worker created successfully"
        );
    }
}
