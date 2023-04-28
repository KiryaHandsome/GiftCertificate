package ru.clevertec.ecl.exception;


import lombok.Getter;

@Getter
public class InvalidOrderException extends RuntimeException {

    private final String orderParam;

    public InvalidOrderException(String message, String orderParam) {
        super(message);
        this.orderParam = orderParam;
    }
}
