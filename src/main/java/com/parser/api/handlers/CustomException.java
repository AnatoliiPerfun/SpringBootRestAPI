package com.parser.api.handlers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;



@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomException extends RuntimeException {
    private static final HttpStatus DEFAULT_STATUS = INTERNAL_SERVER_ERROR;
    HttpStatus httpStatus;

    public CustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CustomException(String message) {
        this(message, DEFAULT_STATUS);
    }
}
