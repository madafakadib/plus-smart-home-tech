package ru.yandex.practicum.api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class BaseApiException extends RuntimeException {
    private final String userMessage;

    protected BaseApiException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }
}
