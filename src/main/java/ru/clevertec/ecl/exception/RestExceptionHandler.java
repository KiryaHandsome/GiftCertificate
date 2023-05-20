package ru.clevertec.ecl.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorEntity> handleEntityNotFoundException(EntityNotFoundException ex) {
        Integer requestedId = ex.getRequestedId();
        String errorMessage = ex.getMessage() + " (id = " + requestedId + ")";
        Integer statusCode = 404;
        String errorCode = String.valueOf(statusCode) + requestedId;
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorEntity(errorCode, errorMessage));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorEntity> handleRuntimeException(RuntimeException ex) {
        String errorMessage = "Internal error: " + ex.getMessage();
        Integer statusCode = 500;
        String errorCode = String.valueOf(statusCode);
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorEntity(errorCode, errorMessage));
    }
}
