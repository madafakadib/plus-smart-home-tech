package ru.yandex.practicum.api.exception;

import lombok.*;

@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ApiException {
    private Object cause;
    private Object stackTrace;
    private String httpStatus;
    private String userMessage;
    private String message;
    private Object suppressed;
    private String localizedMessage;
}
