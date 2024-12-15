package com.koritski.teamsync.backend.controller.worker;

import com.koritski.teamsync.backend.dto.worker.CreateWorkTimeRq;
import com.koritski.teamsync.backend.service.worker.WorkTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/workTime", produces = APPLICATION_JSON_VALUE)
public class WorkTimeController {
    private final WorkTimeService service;

    @PostMapping("start")
    public ResponseEntity<?> startWorkTime(@RequestBody CreateWorkTimeRq rq) {
        return service.startWorkTime(rq);
    }

    @PutMapping("end/{workTimeId}")
    public ResponseEntity<?> endWorkTime(@PathVariable Long workTimeId) {
        return service.endWorkTime(workTimeId);
    }

    @GetMapping("get/current")
    public ResponseEntity<?> getCurrentWorkTIme(@RequestHeader(AUTH_HEADER) String token) {
        return service.getCurrentWorkTime(token);
    }
}
