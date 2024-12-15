package com.koritski.teamsync.hiring.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class Response {
    public <T> ResponseEntity<CommonResponseDto<T>> send(HttpStatus status, String description, T body) {
        CommonResponseDto<T> rs = new CommonResponseDto<T>()
                .setStatus(status.value())
                .setDescription(description)
                .setPayload(body);

        return ResponseEntity
                .status(status)
                .body(rs);
    }

    public <T> ResponseEntity<CommonResponseDto<T>> send(HttpStatus status, T body) {
        CommonResponseDto<T> rs = new CommonResponseDto<T>()
                .setStatus(status.value())
                .setPayload(body);

        return ResponseEntity
                .status(status)
                .body(rs);
    }

    public ResponseEntity<CommonResponseDto<?>> send(HttpStatus status, String description) {
        CommonResponseDto<?> rs = new CommonResponseDto<>()
                .setStatus(status.value())
                .setDescription(description);

        return ResponseEntity
                .status(status)
                .body(rs);
    }

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class CommonResponseDto<T> {
        private int status;
        private String description;
        private T payload;
    }
}
