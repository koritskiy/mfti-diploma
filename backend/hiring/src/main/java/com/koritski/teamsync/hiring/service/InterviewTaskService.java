package com.koritski.teamsync.hiring.service;

import com.koritski.teamsync.hiring.util.Response;
import com.koritski.teamsync.hiring.dto.CreateInterviewTaskRq;
import com.koritski.teamsync.hiring.entity.InterviewTask;
import com.koritski.teamsync.hiring.entity.TaskCategory;
import com.koritski.teamsync.hiring.repository.InterviewTaskRepository;
import com.koritski.teamsync.hiring.repository.TaskCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewTaskService {
    private final InterviewTaskRepository interviewTaskRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    /**
     * Метод создания задачи в категории задач
     * @param rq тело запроса с данными по новой задаче
     * @return
     */
    public ResponseEntity<?> createInterviewTask(CreateInterviewTaskRq rq) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(rq.getTaskCategoryId());

        if (taskCategory.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task category with id = %s not found.", rq.getTaskCategoryId())
            );
        }

        InterviewTask interviewTask = new InterviewTask()
                .setTaskCategory(taskCategory.get())
                .setName(rq.getName())
                .setQuestion(rq.getQuestion())
                .setAnswer(rq.getAnswer())
                .setAuthor(rq.getAuthor());

        interviewTaskRepository.save(interviewTask);

        return Response.send(
                HttpStatus.OK,
                interviewTask
        );
    }

    /**
     * Метод редактирования задачи в базе задач
     * @param rq обновленная информация по задаче
     * @param id id-задачи
     * @return http-ответ
     */
    public ResponseEntity<?> updateInterviewTask(CreateInterviewTaskRq rq, Long id) {
        Optional<InterviewTask> interviewTask = interviewTaskRepository.findById(id);

        if (interviewTask.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Interview task with id = %s not found.", id)
            );
        }

        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(rq.getTaskCategoryId());

        if (taskCategory.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task category with id = %s not found.", rq.getTaskCategoryId())
            );
        }

        interviewTask.get()
                .setTaskCategory(taskCategory.get())
                .setName(rq.getName())
                .setQuestion(rq.getQuestion())
                .setAnswer(rq.getAnswer())
                .setAuthor(rq.getAuthor());

        InterviewTask updatedInterviewTask = interviewTaskRepository.save(interviewTask.get());

        return Response.send(
                HttpStatus.OK,
                updatedInterviewTask
        );
    }

    /**
     * Метод поиска всех задач по определенной категории
     * @param categoryId id-категории
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> findAllTasksByCategory(Long categoryId, String token) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(categoryId);
        if (taskCategory.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task category with id = %s not found.", categoryId)
            );
        }

        List<InterviewTask> tasks = interviewTaskRepository.findAllByTaskCategory(taskCategory.get());

        return Response.send(
                HttpStatus.OK,
                tasks
        );
    }
}
