package com.koritski.teamsync.backend.service.task;

import com.koritski.teamsync.backend.dto.task.CreateBoardColumnRq;
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

import java.util.Optional;

import static com.koritski.teamsync.backend.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardColumnService {
    private final BoardColumnRepository boardColumnRepository;
    private final TaskBoardRepository taskBoardRepository;
    private final OrganizationRepository organizationRepository;
    private final WorkerRepository workerRepository;
    private final JwtUtil jwtUtil;

    /**
     * Создание колонки на доске задач
     * @param rq запрос с данными о новой колонке задач
     * @return http-ответ
     */
    public ResponseEntity<?> createBoardColumn(CreateBoardColumnRq rq, String token) {
        Optional<TaskBoard> taskBoard = taskBoardRepository.findById(rq.getBoardId());

        if (taskBoard.isEmpty()) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    String.format("Task board with id = %s not found.", rq.getBoardId())
            );
        }

        if (!checkIfWorkerInOrganization(token, taskBoard.get().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        BoardColumn boardColumn = new BoardColumn()
                .setName(rq.getName())
                .setBoard(taskBoard.get());

        boardColumnRepository.save(boardColumn);

        return Response.send(
                HttpStatus.OK,
                boardColumn
        );
    }

    /**
     * Получение колонки задач по id
     * @param id id-колонки
     * @param token токен пользователя
     * @return http-ответ
     */
    public ResponseEntity<?> getBoardColumnById(Long id, String token) {
        Optional<BoardColumn> boardColumn = boardColumnRepository.findById(id);
        if (boardColumn.isEmpty()) {
            return Response.send(
                    HttpStatus.NOT_FOUND,
                    String.format("Board column with id = %s not found.", id)
            );
        }

        if (!checkIfWorkerInOrganization(token, boardColumn.get().getBoard().getOrganization().getId())) {
            return Response.send(
                    HttpStatus.BAD_REQUEST,
                    PERMISSION_DESCRIPTION
            );
        }

        return Response.send(
                HttpStatus.OK,
                boardColumn.get()
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
