package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final Integer requestedId;

    public EntityNotFoundException(String message, Integer requestedId) {
        super(message);
        this.requestedId = requestedId;
    }
}
