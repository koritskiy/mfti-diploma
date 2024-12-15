package com.koritski.teamsync.backend.service.organization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koritski.teamsync.backend.dto.organization.CreateOrganizationRq;
import com.koritski.teamsync.backend.dto.organization.FillBankInfoRq;
import com.koritski.teamsync.backend.dto.rs.GetMyOrganizationsRs;
import com.koritski.teamsync.backend.dto.rs.OrganizationStatRs;
import com.koritski.teamsync.backend.dto.rs.OrganizationWorkersStatRs;
import com.koritski.teamsync.backend.entity.WorkerStatusEnum;
import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.entity.task.Task;
import com.koritski.teamsync.backend.entity.worker.WorkTime;
import com.koritski.teamsync.backend.repository.organization.OrganizationRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.repository.task.TaskRepository;
import com.koritski.teamsync.backend.repository.worker.WorkTimeRepository;
import com.koritski.teamsync.backend.util.JwtUtil;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final WorkerRepository workerRepository;
    private final WorkTimeRepository workTimeRepository;
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    /**
     * Метод создания организации
     *
     * @param rq    тело запроса (название, адрес, ссылка)
     * @param token токен пользователя
     * @return http-ответ
     * @throws JsonProcessingException во время проверки пользователя в authentication может вернуться некорректный json
     */
    public ResponseEntity<?> createOrganization(CreateOrganizationRq rq, String token) throws JsonProcessingException {
        Long userId = jwtUtil.getUserIdFromToken(token);
        Organization organization = new Organization()
                .setName(rq.getName())
                .setAddress(rq.getAddress())
                .setLink(rq.getLink())
                .setOwnerUserId(userId);

        organization = organizationRepository.save(organization);

        // Владелец организации - тоже сотрудник
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // todo вынести строку в проперти
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8082/api/auth/v1/user/get/" + userId,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());
        if (root.path("status").asInt() == 404) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    root.path("description").asText()
            );
        }

        String firstName = root.path("payload").path("firstName").asText();
        String lastName = root.path("payload").path("lastName").asText();

        Worker worker = new Worker()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(jwtUtil.getUserEmailFromToken(token))
                .setUserId(userId)
                .setStatus(WorkerStatusEnum.WORK.name())
                .setOrganization(organization)
                .setIsOrganizationAdmin(true);
        workerRepository.save(worker);

        return Response.send(
                HttpStatus.CREATED,
                organization
        );
    }

    /**
     * Заполнение финансовых данных организации
     *
     * @param organizationId id-организации
     * @param rq             запрос с финансовыми данными
     * @param token          токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> fillBankInfoForOrganization(Long organizationId, FillBankInfoRq rq, String token) {
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

        organization.get()
                .setFullOrganizationTitle(rq.getFullOrganizationTitle())
                .setInn(rq.getInn())
                .setKpp(rq.getKpp())
                .setAccountNumber(rq.getAccountNumber())
                .setOrganizationBank(rq.getOrganizationBank())
                .setBankBik(rq.getBankBik())
                .setBankAccountNumber(rq.getBankAccountNumber());

        Organization updatedOrganization = organizationRepository.save(organization.get());
        return Response.send(
                HttpStatus.OK,
                updatedOrganization
        );
    }

    /**
     * Изменение информации про организацию
     *
     * @param rq             запрос с новыми параметрами
     * @param organizationId id-организации
     * @return http-ответ
     */
    public ResponseEntity<?> editOrganizationInfo(CreateOrganizationRq rq, Long organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        organization.get()
                .setName(rq.getName())
                .setLink(rq.getLink())
                .setAddress(rq.getAddress());
        Organization updatedOrganization = organizationRepository.save(organization.get());

        return Response.send(
                HttpStatus.OK,
                updatedOrganization
        );
    }

    /**
     * Получение организации по id
     *
     * @param organizationId id-организации
     * @param token          токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getOrganizationById(Long organizationId, String token) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        boolean isUserWorkerOfOrganization = false;
        for (int i = 0; i < organization.get().getWorkers().size(); i++) {
            if (Objects.equals(organization.get().getWorkers().get(i).getUserId(), userId)) {
                isUserWorkerOfOrganization = true;
            }
        }

        if (!Objects.equals(userId, organization.get().getOwnerUserId())
                && !isUserWorkerOfOrganization) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        return Response.send(
                HttpStatus.OK,
                organization.get()
        );
    }

    /**
     * Получение всех организаций
     *
     * @return http-ответ
     */
    public ResponseEntity<?> getAllOrganizations() {
        return Response.send(
                HttpStatus.OK,
                organizationRepository.findAll()
        );
    }

    /**
     * Получение организаций по id-владельца
     *
     * @param ownerUserId id-пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getOrganizationsByOwnerId(Long ownerUserId) {
        // todo проверка на существование пользователя
        List<Organization> organizations = organizationRepository.findAllByOwnerUserId(ownerUserId);
        return Response.send(
                HttpStatus.OK,
                organizations
        );
    }

    /**
     * Метод для получения организаций, в которых пользователь или владелец, или сотрудник. То есть к таким
     * организациям пользователь имеет доступ
     *
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getMyOrganizations(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);

        List<Organization> userOrganizations = organizationRepository.findAllByOwnerUserId(userId);
        if (userOrganizations.size() > 0) {
            GetMyOrganizationsRs rs = new GetMyOrganizationsRs()
                    .setCount((long) userOrganizations.size())
                    .setOrganizations(userOrganizations);

            return Response.send(HttpStatus.OK, rs);
        }

        Optional<Worker> worker = workerRepository.findByUserId(userId);
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    "Something went wrong."
            );
        }

        Optional<Organization> workerOrganization
                = organizationRepository.findById(worker.get().getOrganization().getId());
        if (workerOrganization.isPresent()) {
            GetMyOrganizationsRs rs = new GetMyOrganizationsRs()
                    .setCount(1L)
                    .setOrganizations(Collections.singletonList(workerOrganization.get()));

            return Response.send(HttpStatus.OK, rs);
        }

        return Response.send(
                HttpStatus.OK,
                new GetMyOrganizationsRs().setCount(0L).setOrganizations(Collections.emptyList())
        );
    }

    /**
     * Добавление администратора в организацию
     *
     * @param organizationId id-организации
     * @param workerId       id-сотрудника, которому выдают права администратора
     * @param token          токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> addAdminForOrganization(Long organizationId, Long workerId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);

        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        Optional<Worker> workerCurrentAdmin = workerRepository.findByUserId(userId);
        if (workerCurrentAdmin.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with user id = %s not found.", userId)
            );
        }

        if (!workerCurrentAdmin.get().getIsOrganizationAdmin()) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        if (!Objects.equals(workerCurrentAdmin.get().getOrganization().getId(), organization.get().getId())) {
            return Response.send(
                    HttpStatus.FORBIDDEN,
                    PERMISSION_DESCRIPTION
            );
        }

        Optional<Worker> workerNewAdmin = workerRepository.findById(workerId);
        if (workerNewAdmin.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s not found.", workerId)
            );
        }

        workerNewAdmin.get().setIsOrganizationAdmin(true);
        Worker updatedWorkerNewAdmin = workerRepository.save(workerNewAdmin.get());

        return Response.send(
                HttpStatus.OK,
                updatedWorkerNewAdmin
        );
    }

    /**
     * Удаление администраторов из организации
     *
     * @param organizationId id-организации
     * @param workerId       id-сотрудника
     * @param token          токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> deleteAdminFromOrganization(Long organizationId, Long workerId, String token) {
        // Из организации админов может удалять только владелец компании
        // Владелец компании отказаться от своих прав не может

        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        if (!Objects.equals(organization.get().getOwnerUserId(), userId)) {
            return Response.send(HttpStatus.FORBIDDEN, PERMISSION_DESCRIPTION);
        }

        Optional<Worker> worker = workerRepository.findById(workerId);
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with id = %s not found.", workerId)
            );
        }

        if (Objects.equals(worker.get().getUserId(), userId)) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    "You cannot delete yourself from the administrators."
            );
        }

        worker.get().setIsOrganizationAdmin(false);
        Worker updatedWorker = workerRepository.save(worker.get());

        return Response.send(
                HttpStatus.OK,
                updatedWorker
        );
    }

    /**
     * Получение статистики организации
     *
     * @param organizationId id-организации
     * @param token          токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getStatsForOrganization(Long organizationId, String token) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        Long countActualWorkers = organization.get().getWorkers().stream()
                .filter(w -> Objects.equals(w.getStatus(), WorkerStatusEnum.WORK.name()))
                .count();
        Long countFiredWorkers = organization.get().getWorkers().stream()
                .filter(w -> Objects.equals(w.getStatus(), WorkerStatusEnum.FIRED.name()))
                .count();

        long countTasks = 0;
        for (int i = 0; i < organization.get().getTaskBoards().size(); i++) {
            for (int bc = 0; bc < organization.get().getTaskBoards().get(i).getBoardColumns().size(); bc++) {
                countTasks += organization.get().getTaskBoards().get(i).getBoardColumns().get(bc).getTasks().size();
            }
        }

        long totalSalary = 0;
        for (int i = 0; i < organization.get().getWorkers().size(); i++) {
            List<WorkTime> workTimes = workTimeRepository.findAllByWorker(organization.get().getWorkers().get(i));
            for (WorkTime workTime : workTimes) {
                totalSalary += workTime.getTotal();
            }
        }

        OrganizationStatRs rs = new OrganizationStatRs()
                .setCountActualWorkers(countActualWorkers)
                .setCountFiredWorkers(countFiredWorkers)
                .setCountTaskBoards((long) organization.get().getTaskBoards().size())
                .setCountTasks(countTasks)
                .setTotalSalary(totalSalary);

        return Response.send(
                HttpStatus.OK,
                rs
        );
    }

    public ResponseEntity<?> getStatsForOrganizationWorkers(Long organizationId, String token) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        List<Worker> workers = workerRepository.findAllByOrganization(organization.get());
        List<OrganizationWorkersStatRs.EachWorker> rsWorkers = new ArrayList<>();

        for (Worker worker : workers) {
            List<WorkTime> workTimes = workTimeRepository.findAllByWorker(worker);
            long totalCash = 0;
            for (WorkTime workTime : workTimes) {
                totalCash += workTime.getTotal();
            }

            List<Task> tasks = taskRepository.findAllByWorker(worker);
            long totalTasks = tasks.size();

            rsWorkers.add(
                    new OrganizationWorkersStatRs.EachWorker()
                            .setName(worker.getFirstName())
                            .setSurname(worker.getLastName())
                            .setEmail(worker.getEmail())
                            .setTotalCash(String.valueOf(totalCash))
                            .setTotalTaskOnWorker(String.valueOf(totalTasks))
            );
        }

        return Response.send(
                HttpStatus.OK,
                rsWorkers
        );
    }
}
