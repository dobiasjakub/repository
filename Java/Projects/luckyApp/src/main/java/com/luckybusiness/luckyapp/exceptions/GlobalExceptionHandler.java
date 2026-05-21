package com.luckybusiness.luckyapp.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponseMessage> handleResponseStatusExceptions(ResponseStatusException ex, HttpServletRequest request) {
        var response = new ExceptionResponseMessage();
        response.setStatus(ex.getStatusCode().value());
        response.setMessage(ex.getReason());
        response.setPath(request.getRequestURI());
        response.setError(ex.getMessage());

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}
