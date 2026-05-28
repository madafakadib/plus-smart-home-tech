package ru.yandex.practicum.shopping_store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.api.exception.ApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundRuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleProductNotFound(ProductNotFoundRuntimeException ex) {

        List<Map<String, Object>> stackTraceList = Arrays.stream(ex.getStackTrace())
                .map(element -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("classLoaderName", null);
                    map.put("moduleName", null);
                    map.put("moduleVersion", null);
                    map.put("methodName", element.getMethodName());
                    map.put("fileName", element.getFileName());
                    map.put("lineNumber", element.getLineNumber());
                    map.put("className", element.getClassName());
                    map.put("nativeMethod", element.isNativeMethod());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> causeObj = new java.util.HashMap<>();
        causeObj.put("stackTrace", stackTraceList);
        causeObj.put("message", ex.getMessage());
        causeObj.put("localizedMessage", ex.getLocalizedMessage());

        List<Object> suppressedList = Collections.emptyList();

        return ApiException.builder()
                .cause(causeObj)
                .stackTrace(stackTraceList)
                .httpStatus("404 NOT_FOUND")
                .userMessage("Ошибка, товар по идентификатору в БД не найден")
                .message(ex.getMessage())
                .suppressed(suppressedList)
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }
}
