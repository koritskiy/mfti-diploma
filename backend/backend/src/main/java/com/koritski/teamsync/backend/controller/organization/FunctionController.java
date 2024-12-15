package com.koritski.teamsync.backend.controller.organization;

import com.koritski.teamsync.backend.dto.organization.CreateFunctionRq;
import com.koritski.teamsync.backend.service.organization.FunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/function", produces = APPLICATION_JSON_VALUE)
public class FunctionController {
    private final FunctionService service;

    @PostMapping("create")
    public ResponseEntity<?> createFunction(@RequestBody CreateFunctionRq rq,
                                            @RequestHeader(AUTH_HEADER) String token) {
        return service.createFunction(rq, token);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteFunction(@PathVariable Long id,
                                            @RequestHeader(AUTH_HEADER) String token) {
        return service.deleteFunction(id, token);
    }
}
