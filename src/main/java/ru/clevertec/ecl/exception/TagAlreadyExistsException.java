package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class TagAlreadyExistsException extends RuntimeException {

    private final String name;

    public TagAlreadyExistsException(String message, String name) {
        super(message);
        this.name = name;
    }
}
