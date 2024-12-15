package com.koritski.teamsync.hiring.controller;

import com.koritski.teamsync.hiring.dto.CreateTaskCategoryRq;
import com.koritski.teamsync.hiring.service.TaskCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.hiring.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/hiring/v1/taskCategory", produces = APPLICATION_JSON_VALUE)
public class TaskCategoryController {
    private final TaskCategoryService taskCategoryService;

    @PostMapping("create")
    public ResponseEntity<?> createTaskCategory(@RequestBody CreateTaskCategoryRq rq) {
        return taskCategoryService.createTaskCategory(rq);
    }

    @PutMapping("update/{taskCategoryId}")
    public ResponseEntity<?> updateTaskCategory(@RequestBody CreateTaskCategoryRq rq,
                                                @PathVariable Long taskCategoryId) {
        return taskCategoryService.updateTaskCategory(rq, taskCategoryId);
    }

    @GetMapping("findCategory")
    public ResponseEntity<?> findTaskCategory(@RequestBody String name) {
        return taskCategoryService.findTaskCategory(name);
    }

    @GetMapping("all/{organizationId}")
    public ResponseEntity<?> findAllCategoriesInOrganization(@PathVariable Long organizationId,
                                                             @RequestHeader(AUTH_HEADER) String token) {
        return taskCategoryService.findAllCategoriesInOrganization(organizationId, token);
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId,
                                             @RequestHeader(AUTH_HEADER) String token) {
        return taskCategoryService.getCategoryById(categoryId, token);
    }
}
