package com.koritski.teamsync.backend.controller.organization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.koritski.teamsync.backend.dto.organization.*;
import com.koritski.teamsync.backend.dto.organization.DismissWorkerRq;
import com.koritski.teamsync.backend.dto.worker.FillWorkerPayInfoRq;
import com.koritski.teamsync.backend.service.organization.WorkerService;
import com.koritski.teamsync.backend.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/worker", produces = APPLICATION_JSON_VALUE)
public class WorkerController {
    private final WorkerService workerService;

    @PostMapping("create")
    public ResponseEntity<?> createWorker(@RequestBody CreateWorkerRq rq,
                                          @RequestHeader(AUTH_HEADER) String token) throws JsonProcessingException {
        return workerService.createWorker(rq, token);
    }

    @PutMapping("addFunctionForWorker")
    public ResponseEntity<?> addFunctionForWorker(@RequestBody AddFunctionForWorkerRq rq,
                                                  @RequestHeader(AUTH_HEADER) String token) {
        return workerService.addFunctionForWorker(rq, token);
    }

    @PutMapping("dismiss")
    public ResponseEntity<?> dismissWorker(@RequestBody DismissWorkerRq rq,
                                           @RequestHeader(AUTH_HEADER) String token) {
        return workerService.dismissWorker(rq, token);
    }

    // todo история устройства сотрудников - в микросервис к Маше
    // todo как обработать случай, когда сотрудник был сначала в одной организации, а потом - в другой?
    @PutMapping("rehire")
    public ResponseEntity<?> rehireWorker(@RequestBody RehireWorkerRq rq,
                                          @RequestHeader(AUTH_HEADER) String token) {
        return workerService.rehireWorker(rq, token);
    }

    @GetMapping("getWorkersByOrganization/{organizationId}")
    public ResponseEntity<?> getWorkersByOrganization(@PathVariable Long organizationId,
                                                      @RequestHeader(AUTH_HEADER) String token) {
        return workerService.getWorkers(organizationId, token);
    }

    @GetMapping("getActual/{organizationId}")
    public ResponseEntity<?> getActualWorkers(@PathVariable Long organizationId,
                                              @RequestHeader(AUTH_HEADER) String token) {
        return workerService.getActualWorkers(organizationId, token);
    }

    @GetMapping("getFired/{organizationId}")
    public ResponseEntity<?> getFiredWorkers(@PathVariable Long organizationId,
                                             @RequestHeader(AUTH_HEADER) String token) {
        return workerService.getFiredWorkers(organizationId, token);
    }

    // Контроллеры для работы самого сотрудника, администратор организации к этому урлу доступ не имеет
    @PostMapping("fill-pay-info/{workerId}")
    public ResponseEntity<?> fillWorkerPayInfo(@PathVariable Long workerId,
                                               @RequestBody FillWorkerPayInfoRq rq,
                                               @RequestHeader(AUTH_HEADER) String token) {
        return workerService.fillWorkerPayInfo(workerId, rq, token);
    }

    @GetMapping("get/{workerId}")
    public ResponseEntity<?> getWorkerById(@PathVariable Long workerId,
                                           @RequestHeader(AUTH_HEADER) String token) {
        return workerService.getWorkerById(workerId, token);
    }

    @GetMapping("me")
    public ResponseEntity<?> getWorkerByUserId(@RequestHeader(AUTH_HEADER) String token) {
        return workerService.getWorkerByUserId(token);
    }
}
