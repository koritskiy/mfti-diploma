package com.koritski.teamsync.backend.controller.task;

import com.koritski.teamsync.backend.dto.task.AssigneeTaskRq;
import com.koritski.teamsync.backend.dto.task.CreateTaskRq;
import com.koritski.teamsync.backend.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/task", produces = APPLICATION_JSON_VALUE)
public class TaskController {
    private final TaskService service;

    @PostMapping("create")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRq rq,
                                        @RequestHeader(AUTH_HEADER) String token) {
        return service.createTask(rq, token);
    }

    @PutMapping("assign")
    public ResponseEntity<?> assignTask(@RequestBody AssigneeTaskRq rq,
                                        @RequestHeader(AUTH_HEADER) String token) {
        return service.assignTask(rq, token);
    }

    @PutMapping("remove/assign/{id}")
    public ResponseEntity<?> removeAssignFromTask(@PathVariable Long id,
                                                  @RequestHeader(AUTH_HEADER) String token) {
        return service.removeAssignFromTask(id, token);
    }

    @PutMapping("updateStatus/{boardColumnId}/{taskId}")
    public ResponseEntity<?> changeStatus(@PathVariable Long boardColumnId, @PathVariable Long taskId,
                                          @RequestHeader(AUTH_HEADER) String token) {
        return service.changeStatus(boardColumnId, taskId, token);
    }

    @GetMapping("get/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId,
                                     @RequestHeader(AUTH_HEADER) String token) {
        return service.getTask(taskId, token);
    }

    @DeleteMapping("delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId,
                                        @RequestHeader(AUTH_HEADER) String token) {
        return service.deleteTask(taskId, token);
    }

    @PutMapping("edit/{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId,
                                      @RequestBody CreateTaskRq rq,
                                      @RequestHeader(AUTH_HEADER) String token) {
        return service.editTask(taskId, rq, token);
    }

    @GetMapping("all/my")
    public ResponseEntity<?> getAllMyTasks(@RequestHeader(AUTH_HEADER) String token) {
        return service.getAllMyTasks(token);
    }
}
