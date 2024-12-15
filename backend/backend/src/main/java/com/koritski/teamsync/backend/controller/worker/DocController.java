package com.koritski.teamsync.backend.controller.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koritski.teamsync.backend.dto.worker.GetPaymentByWorkerRq;
import com.koritski.teamsync.backend.service.worker.DocService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static com.koritski.teamsync.backend.constants.AuthConstants.AUTH_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/tracker/v1/doc", produces = APPLICATION_JSON_VALUE)
@Slf4j
public class DocController {
    private final DocService docService;

    @PostMapping(value = "get", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getDocForPayment(@RequestBody GetPaymentByWorkerRq rq,
                                   @RequestHeader(AUTH_HEADER) String token)
            throws JsonProcessingException, NotFoundException {
        return docService.getDocForPayment(rq, token);
    }
}
