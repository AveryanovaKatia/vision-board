package ru.project.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.project.controllers.FeedbackController;
import ru.project.controllers.PostController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {PostController.class, FeedbackController.class})
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final NotFoundException exception) {
        log.debug("Получен статус 404 NOT_FOUND{}", exception.getMessage(), exception);
        return createMap("NOT_FOUND", "Объект не найден", exception.getMessage());
    }

    @ExceptionHandler({ImageProcessingException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handlerInternal(final Exception exception) {
        log.debug("Получен статус 500 INTERNAL_SERVER_ERROR {}", exception.getMessage(), exception);
        return createMap("INTERNAL_SERVER_ERROR", "Ошибка", exception.getMessage());
    }

    private Map<String, String> createMap(String status, String reason, String message) {
        return Map.of("status", status,
                "reason", reason,
                "message", message,
                "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
