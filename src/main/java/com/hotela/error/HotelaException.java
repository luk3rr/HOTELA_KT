package com.hotela.error;

import lombok.Getter;

import java.util.UUID;

@Getter
public sealed class HotelaException extends RuntimeException {
    private final String code;

    public HotelaException(String code, String message) {
        super(message);
        this.code = code;
    }

    public static final class ExampleNotFoundException extends HotelaException {
        public ExampleNotFoundException(UUID id) {
            super("000", "Example with id " + id + " not found");
        }
    }
}