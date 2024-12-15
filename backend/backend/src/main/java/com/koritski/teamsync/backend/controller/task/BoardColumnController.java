package com.koritski.teamsync.backend.controller.task;

import com.koritski.teamsync.backend.dto.task.CreateBoardColumnRq;
import com.koritski.teamsync.backend.service.task.BoardColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/boardColumn", produces = APPLICATION_JSON_VALUE)
public class BoardColumnController {
    private final BoardColumnService service;

    @PostMapping("create")
    public ResponseEntity<?> createBoardColumn(@RequestBody CreateBoardColumnRq rq,
                                               @RequestHeader(AUTH_HEADER) String token) {
        return service.createBoardColumn(rq, token);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getBoardColumnById(@PathVariable Long id,
                                                @RequestHeader(AUTH_HEADER) String token) {
        return service.getBoardColumnById(id, token);
    }
}
