package com.koritski.teamsync.backend.service.task;

import com.koritski.teamsync.backend.dto.task.CreateGitHubBranchRq;
import com.koritski.teamsync.backend.entity.task.GitHubBranch;
import com.koritski.teamsync.backend.entity.task.Task;
import com.koritski.teamsync.backend.repository.task.GitHubBranchRepository;
import com.koritski.teamsync.backend.repository.task.TaskRepository;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {
    private final GitHubBranchRepository gitHubBranchRepository;
    private final TaskRepository taskRepository;

    /**
     * Метод связывания задачи и ветки в GitHub
     * @param rq данные связанной ветки
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> createGitHubBranch(CreateGitHubBranchRq rq, String token) {
        Optional<Task> task = taskRepository.findById(rq.getTaskId());
        if (task.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Task with id = %s not found.", rq.getTaskId())
            );
        }

        GitHubBranch newBranch = new GitHubBranch()
                .setRepoFullName(rq.getRepoFullName())
                .setBranchName(rq.getBranchName())
                .setRef(rq.getRef())
                .setTask(task.get());
        newBranch = gitHubBranchRepository.save(newBranch);

        return Response.send(
                HttpStatus.CREATED,
                newBranch
        );
    }
}
