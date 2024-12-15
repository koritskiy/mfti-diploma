package com.koritski.teamsync.hiring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.koritski.teamsync.hiring.constants.AuthConstants.PERMISSION_DESCRIPTION;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e) {
        log.error("Exception handler caught: ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(PERMISSION_DESCRIPTION); // todo
    }
}