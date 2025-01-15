package com.pragma.tecnologia.infrastructure.configuration;

import com.pragma.tecnologia.domain.exception.DuplicatedTechnologyNameException;
import com.pragma.tecnologia.infrastructure.commons.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedTechnologyNameException.class)
    public Mono<ResponseEntity<String>> handleDuplicatedException(DuplicatedTechnologyNameException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<String>> handleValidationException(ServerWebInputException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Constants.ERROR_INVALID + ex.getReason()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Constants.ERROR_ARGUMENT_INVALID + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGeneralException(Exception ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Constants.ERROR_INTERN+ ex.getMessage()));
    }
}