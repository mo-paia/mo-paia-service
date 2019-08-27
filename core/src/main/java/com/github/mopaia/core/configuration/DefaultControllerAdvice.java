package com.github.mopaia.core.configuration;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity PSQLExceptionHandler(PSQLException e) {
        log.error("PSQLException caught on DefaultControllerAdvice, bad Request will be answered on response.", e);
        return ResponseEntity.badRequest().build();
    }
}
