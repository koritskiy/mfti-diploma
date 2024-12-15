package com.koritski.teamsync.backend.service.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koritski.teamsync.backend.dto.rs.SendAssignTaskViaKafkaRs;
import com.koritski.teamsync.backend.dto.task.AssigneeTaskRq;
import com.koritski.teamsync.backend.dto.task.CreateTaskRq;
import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.entity.task.BoardColumn;
import com.koritski.teamsync.backend.entity.task.Task;
import com.koritski.teamsync.backend.entity.task.TaskTag;
import com.koritski.teamsync.backend.repository.organization.OrganizationRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.repository.task.BoardColumnRepository;
import com.koritski.teamsync.backend.repository.task.TaskRepository;
import com.koritski.teamsync.backend.repository.task.TaskTagRepository;
import com.koritski.teamsync.backend.util.JwtUtil;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.koritski.teamsync.backend.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final WorkerRepository workerRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final OrganizationRepository organizationRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Метод создания задачи
     *
     * @param rq запрос с данными по задаче
     * @return http-ответ
     */
    public ResponseEntity<?> createTask(CreateTaskRq rq, String token) {
        Optional<BoardColumn> boardColumn = boardColumnRepository.findById(rq.getBoardColumnId());

        if (boardColumn.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Board Column with id = %s not found.", rq.getBoardColumnId())
            );
        }

        if (!checkIfWorkerInOrganization(token, boardColumn.get().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        Task task = new Task()
                .setName(rq.getName())
                .setDescription(rq.getDescription())
                .setStatus(boardColumn.get().getName())
                .setBoardColumn(boardColumn.get());

        taskRepository.save(task);

        return Response.send(
                HttpStatus.OK,
                task
        );
    }

    /**
     * Привязывание задачи к определенному сотруднику
     *
     * @param rq запрос с данными по задаче и сотруднику
     * @return http-ответ
     */
    @Transactional
    public ResponseEntity<?> assignTask(AssigneeTaskRq rq, String token) {
        Optional<Task> task = taskRepository.findById(rq.getTaskId());
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Task with id = %s not found.", rq.getTaskId())
            );
        }

        if (!checkIfWorkerInOrganization(token, task.get().getBoardColumn().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        Optional<Worker> worker = workerRepository.findById(rq.getWorkerId());
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Worker with id = %s not found.", rq.getWorkerId())
            );
        }

        if (task.get().getWorker() != null) {
            // todo история перемещений между участниками
            task.get().setWorker(null);
            taskRepository.save(task.get());
        }

        task.get().setWorker(worker.get());
        Task updatedTask = taskRepository.save(task.get());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8082/api/auth/v1/user/get/" + updatedTask.getWorker().getUserId(),
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String tgChatId = root.path("payload").path("tgChatId").asText();

            if (!tgChatId.isEmpty()) {
                SendAssignTaskViaKafkaRs rs = new SendAssignTaskViaKafkaRs()
                        .setTaskName(updatedTask.getName())
                        .setTaskDescription(updatedTask.getDescription())
                        .setTaskStatus(updatedTask.getStatus())
                        .setUserTgChatId(Long.valueOf(tgChatId));
                kafkaTemplate.send("TEAMSYNC.ASSIGN_TASK.V1", objectMapper.writeValueAsString(rs));
            }
        } catch (Exception e) {
            log.error("Message to kafka not send.");
            log.error(e.getMessage());
        }

        return Response.send(
                HttpStatus.OK,
                updatedTask
        );
    }

    /**
     * Отвязать задачу от сотрудника
     *
     * @param id id-задачи
     * @return http-ответ
     */
    public ResponseEntity<?> removeAssignFromTask(Long id, String token) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task with id = %s not found.", id)
            );
        }

        if (!checkIfWorkerInOrganization(token, task.get().getBoardColumn().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        task.get().setWorker(null);
        taskRepository.save(task.get());
        return Response.send(
                HttpStatus.OK,
                task.get()
        );
    }

    /**
     * Изменение статуса задачи (задача привязывается к определенной колонке)
     *
     * @param boardColumnId id-новой колонки
     * @param taskId        id-задачи
     * @return http-ответ
     */
    @Transactional
    public ResponseEntity<?> changeStatus(Long boardColumnId, Long taskId, String token) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Task with id = %s not found.", taskId)
            );
        }

        if (!checkIfWorkerInOrganization(token, task.get().getBoardColumn().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        Optional<BoardColumn> boardColumn = boardColumnRepository.findById(boardColumnId);
        if (boardColumn.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Board column with id = %s not found.", boardColumnId)
            );
        }

        task.get()
                .setBoardColumn(boardColumn.get())
                .setStatus(boardColumn.get().getName());
        taskRepository.save(task.get());

        Optional<Task> updatedTask = taskRepository.findById(taskId);

        return Response.send(
                HttpStatus.OK,
                updatedTask.get()
        );
    }

    /**
     * Получение задачи по id
     *
     * @param taskId id-задачи
     * @return http-ответ
     */
    public ResponseEntity<?> getTask(Long taskId, String token) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task with id = %s not found.", taskId)
            );
        }

        if (!checkIfWorkerInOrganization(token, task.get().getBoardColumn().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        return Response.send(
                HttpStatus.OK,
                task.get()
        );
    }

    /**
     * Метод удаления задачи
     *
     * @param taskId id-задачи
     * @return http-ответ
     */
    public ResponseEntity<?> deleteTask(Long taskId, String token) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task with id = %s not found.", taskId)
            );
        }

        List<TaskTag> taskTags = taskTagRepository.findAllByTask(task.get());
        taskTagRepository.deleteAll(taskTags);

        taskRepository.delete(task.get());
        return Response.send(
                HttpStatus.OK,
                "Task was deleted successfully."
        );
    }

    public ResponseEntity<?> editTask(Long taskId, CreateTaskRq rq, String token) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task with id = %s not found.", taskId)
            );
        }

        if (!checkIfWorkerInOrganization(token, task.get().getBoardColumn().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        task.get()
                .setName(rq.getName())
                .setDescription(rq.getDescription());
        Task updatedTask = taskRepository.save(task.get());

        return Response.send(
                HttpStatus.OK,
                updatedTask
        );
    }

    public ResponseEntity<?> getAllMyTasks(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Worker> worker = workerRepository.findByUserId(userId);
        if (worker.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Worker with user id = %s not found.")
            );
        }

        List<Task> tasks = taskRepository.findAllByWorker(worker.get());
        return Response.send(
                HttpStatus.OK,
                tasks
        );
    }

    public boolean checkIfWorkerInOrganization(String token, Long organizationId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return false;
        }

        Optional<Worker> worker = workerRepository.findByUserIdAndOrganization(userId, organization.get());
        return worker.isPresent();
    }
}
