package ru.yandex.practicum.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public abstract class BaseApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String userMessage;

    protected BaseApiException(String message, HttpStatus httpStatus, String userMessage) {
        super(message);
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
    }
}
