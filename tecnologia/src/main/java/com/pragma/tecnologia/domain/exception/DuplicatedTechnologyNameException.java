package com.pragma.tecnologia.domain.exception;

public class DuplicatedTechnologyNameException extends RuntimeException {
    public DuplicatedTechnologyNameException(String message) {
        super(message);
    }
}
