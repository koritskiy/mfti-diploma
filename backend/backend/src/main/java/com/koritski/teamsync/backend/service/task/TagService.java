package com.koritski.teamsync.backend.service.task;

import com.koritski.teamsync.backend.dto.task.CreateTagRq;
import com.koritski.teamsync.backend.dto.task.LinkTagToTaskRq;
import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.entity.task.Tag;
import com.koritski.teamsync.backend.entity.task.Task;
import com.koritski.teamsync.backend.entity.task.TaskTag;
import com.koritski.teamsync.backend.repository.organization.OrganizationRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.repository.task.TagRepository;
import com.koritski.teamsync.backend.repository.task.TaskRepository;
import com.koritski.teamsync.backend.repository.task.TaskTagRepository;
import com.koritski.teamsync.backend.util.JwtUtil;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.koritski.teamsync.backend.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;
    private final OrganizationRepository organizationRepository;
    private final JwtUtil jwtUtil;
    private final WorkerRepository workerRepository;

    /**
     * Создание тега
     *
     * @param rq запрос с данными нового тега
     * @return http-ответ
     */
    public ResponseEntity<?> createTag(CreateTagRq rq, String token) {
        Optional<Organization> organization = organizationRepository.findById(rq.getOrganizationId());
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", rq.getOrganizationId())
            );
        }

        if (!checkIfWorkerInOrganization(token, organization.get().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        Tag tag = new Tag()
                .setName(rq.getName())
                .setOrganization(organization.get());
        tag = tagRepository.save(tag);

        return Response.send(
                HttpStatus.CREATED,
                tag
        );
    }

    /**
     * Привязать тег к задаче
     *
     * @param rq запрос с данными о теге и о задаче
     * @return http-ответ
     */
    public ResponseEntity<?> linkTagToTask(LinkTagToTaskRq rq, String token) {
        Optional<Task> task = taskRepository.findById(rq.getTaskId());
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task with id = %s not found.", rq.getTaskId())
            );
        }

        if (!checkIfWorkerInOrganization(token, task.get().getBoardColumn().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        Optional<Tag> tag = tagRepository.findById(rq.getTagId());
        if (tag.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Tag with id = %s not found.", rq.getTagId())
            );
        }

        if (!Objects.equals(
                task.get().getBoardColumn().getBoard().getOrganization().getId(),
                tag.get().getOrganization().getId())
        ) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    "Task and tag from different organizations."
            );
        }

        TaskTag taskTag = new TaskTag()
                .setTask(task.get())
                .setTag(tag.get())
                .setTagValue(tag.get().getName());
        taskTag = taskTagRepository.save(taskTag);

        return Response.send(
                HttpStatus.OK,
                taskTag
        );
    }

    /**
     * Метод получения всех тегов для организации
     *
     * @param organizationId id-организации
     * @return http-ответ
     */
    public ResponseEntity<?> getAllTagsForOrganization(Long organizationId, String token) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Organization with id = %s not found.", organizationId)
            );
        }

        if (!checkIfWorkerInOrganization(token, organization.get().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        List<Tag> tagsForOrganization = tagRepository.findAllByOrganization(organization.get());
        return Response.send(
                HttpStatus.OK,
                tagsForOrganization
        );
    }

    /**
     * Метод отвязывания тега от задач
     *
     * @param id id-тега
     * @return http-ответ
     */
    public ResponseEntity<?> unlinkTagFromTask(Long id, String token) {
        Optional<TaskTag> taskTag = taskTagRepository.findById(id);
        if (taskTag.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Link task and tag with id = %s not found.", id)
            );
        }

        if (!checkIfWorkerInOrganization(token,
                taskTag.get().getTask().getBoardColumn().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        taskTagRepository.deleteById(id);
        return Response.send(
                HttpStatus.OK,
                "Link task and tag was successfully deleted."
        );
    }

    /**
     * Метод удаления тега
     *
     * @param id id-тега
     * @return http-ответ
     */
    @Transactional
    public ResponseEntity<?> deleteTag(Long id, String token) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Tag with id = %s not found.", id)
            );
        }

        if (!checkIfWorkerInOrganization(token, tag.get().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        taskTagRepository.deleteAllByTag(tag.get());
        tagRepository.deleteById(id);

        return Response.send(
                HttpStatus.OK,
                String.format("Tag with id = %s was successfully deleted.", id)
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
