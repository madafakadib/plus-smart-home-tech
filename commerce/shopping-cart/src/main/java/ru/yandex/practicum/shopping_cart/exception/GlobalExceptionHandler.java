package ru.yandex.practicum.shopping_cart.exception;

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

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiException handleNoProductsInCart(NoProductsInShoppingCartException ex) {
        List<Map<String, Object>> stackTraceList = convertStackTrace(ex.getStackTrace());
        Map<String, Object> causeObj = createCauseObject(ex, stackTraceList);

        return ApiException.builder()
                .cause(causeObj)
                .stackTrace(stackTraceList)
                .httpStatus("400 BAD_REQUEST")
                .userMessage("Корзина деактивирована. Изменение содержимого невозможно.")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(NotAuthorizedUserRuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiException handleNotAuthorized(NotAuthorizedUserRuntimeException ex) {
        List<Map<String, Object>> stackTraceList = convertStackTrace(ex.getStackTrace());
        Map<String, Object> causeObj = createCauseObject(ex, stackTraceList);

        return ApiException.builder()
                .cause(causeObj)
                .stackTrace(stackTraceList)
                .httpStatus("401 UNAUTHORIZED")
                .userMessage("Пользователь не авторизован.")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(NotFoundCartException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleCartNotFound(NotFoundCartException ex) {
        List<Map<String, Object>> stackTraceList = convertStackTrace(ex.getStackTrace());
        Map<String, Object> causeObj = createCauseObject(ex, stackTraceList);

        return ApiException.builder()
                .cause(causeObj)
                .stackTrace(stackTraceList)
                .httpStatus("404 NOT_FOUND")
                .userMessage("Корзина не найдена.")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    private List<Map<String, Object>> convertStackTrace(StackTraceElement[] elements) {
        return Arrays.stream(elements)
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
    }

    private Map<String, Object> createCauseObject(Exception ex, List<Map<String, Object>> stackTraceList) {
        Map<String, Object> causeObj = new java.util.HashMap<>();
        causeObj.put("stackTrace", stackTraceList);
        causeObj.put("message", ex.getMessage());
        causeObj.put("localizedMessage", ex.getLocalizedMessage());
        return causeObj;
    }
}
