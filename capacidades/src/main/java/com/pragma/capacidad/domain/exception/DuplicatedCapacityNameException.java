package com.pragma.capacidad.domain.exception;

public class DuplicatedCapacityNameException extends RuntimeException {
    public DuplicatedCapacityNameException(String message) {
        super(message);
    }
}