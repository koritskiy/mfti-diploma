package com.koritski.teamsync.backend.service.task;

import com.koritski.teamsync.backend.dto.task.CreateTaskBoardRq;
import com.koritski.teamsync.backend.entity.organization.Organization;
import com.koritski.teamsync.backend.entity.organization.Worker;
import com.koritski.teamsync.backend.entity.task.BoardColumn;
import com.koritski.teamsync.backend.entity.task.TaskBoard;
import com.koritski.teamsync.backend.repository.organization.OrganizationRepository;
import com.koritski.teamsync.backend.repository.organization.WorkerRepository;
import com.koritski.teamsync.backend.repository.task.BoardColumnRepository;
import com.koritski.teamsync.backend.repository.task.TaskBoardRepository;
import com.koritski.teamsync.backend.util.JwtUtil;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.koritski.teamsync.backend.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskBoardService {
    private final TaskBoardRepository taskBoardRepository;
    private final OrganizationRepository organizationRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final WorkerRepository workerRepository;
    private final JwtUtil jwtUtil;

    /**
     * Метод создания доски задач
     *
     * @param rq запрос с данными доски задач
     * @return http-ответ
     */
    public ResponseEntity<?> createTaskBoard(CreateTaskBoardRq rq, String token) {
        Optional<Organization> organization = organizationRepository.findById(rq.getOrganizationId());

        if (organization.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Organization with id = %s not found.", rq.getOrganizationId())
            );
        }

        if (!checkIfWorkerInOrganization(token, organization.get().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        TaskBoard taskBoard = new TaskBoard()
                .setName(rq.getName())
                .setOrganization(organization.get());

        taskBoard = taskBoardRepository.save(taskBoard);

        BoardColumn boardColumnToDo = new BoardColumn().setBoard(taskBoard).setName("To do");
        BoardColumn boardColumnInProgress = new BoardColumn().setBoard(taskBoard).setName("In progress");
        BoardColumn boardColumnDone = new BoardColumn().setBoard(taskBoard).setName("Done");

        List<BoardColumn> boardColumns = Arrays.asList(boardColumnToDo, boardColumnInProgress, boardColumnDone);
        boardColumns = boardColumnRepository.saveAll(boardColumns);

        taskBoard.setBoardColumns(boardColumns);
        taskBoard = taskBoardRepository.save(taskBoard);

        return Response.send(
                HttpStatus.OK,
                taskBoard
        );
    }

    /**
     * Получение доски задач по id
     *
     * @param taskBoardId id-доски задач
     * @return http-ответ
     */
    public ResponseEntity<?> getTaskBoardById(Long taskBoardId, String token) {
        Optional<TaskBoard> taskBoard = taskBoardRepository.findById(taskBoardId);

        if (taskBoard.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Task board with id = %s not found.", taskBoardId)
            );
        }

        if (!checkIfWorkerInOrganization(token, taskBoard.get().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        return Response.send(
                HttpStatus.OK,
                taskBoard.get()
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
