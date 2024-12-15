package com.koritski.teamsync.hiring.service;

import com.koritski.teamsync.hiring.util.Response;
import com.koritski.teamsync.hiring.dto.CreateTaskCategoryRq;
import com.koritski.teamsync.hiring.entity.TaskCategory;
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
public class TaskCategoryService {
    private final TaskCategoryRepository taskCategoryRepository;

    /**
     * Метод создания категории задач
     * @param rq запрос с данными о новой категории задач
     * @return http-ответ
     */
    public ResponseEntity<?> createTaskCategory(CreateTaskCategoryRq rq) {
        Optional<TaskCategory> taskCategoryOptional = taskCategoryRepository.findByName(rq.getName());

        if (taskCategoryOptional.isPresent()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Task category with name = %s already exists.", rq.getName())
            );
        }

        TaskCategory taskCategory = new TaskCategory()
                .setName(rq.getName())
                .setOrganizationId(rq.getOrganizationId());

        taskCategoryRepository.save(taskCategory);

        return Response.send(
                HttpStatus.OK,
                taskCategory
        );
    }

    /**
     * Метод изменения категории задач
     * @param rq обновленные данные
     * @param id id-категории задач
     * @return http-ответ
     */
    public ResponseEntity<?> updateTaskCategory(CreateTaskCategoryRq rq, Long id) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(id);

        if (taskCategory.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task category with id = %s not found.", id)
            );
        }

        taskCategory.get().setName(rq.getName());
        TaskCategory updatedTaskCategory = taskCategoryRepository.save(taskCategory.get());

        return Response.send(
                HttpStatus.OK,
                updatedTaskCategory
        );
    }

    /**
     * Метод поиска категории задач по названию
     * @param name название категории задач
     * @return http-ответ
     */
    public ResponseEntity<?> findTaskCategory(String name) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findByName(name);

        if (taskCategory.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task category with nae = %s not found.", name)
            );
        }

        return Response.send(
                HttpStatus.OK,
                taskCategory
        );
    }

    /**
     * Поиск всех категорий задач в организации
     * @param organizationId id-организации
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> findAllCategoriesInOrganization(Long organizationId, String token) {
        List<TaskCategory> taskCategories = taskCategoryRepository.findAllByOrganizationId(organizationId);

        return Response.send(
                HttpStatus.OK,
                taskCategories
        );
    }

    /**
     * Получение категории по id
     * @param categoryId id-категории
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getCategoryById(Long categoryId, String token) {
        Optional<TaskCategory> category = taskCategoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Category with id = %s not found.", categoryId)
            );
        }

        return Response.send(
                HttpStatus.OK,
                category.get()
        );
    }
}
