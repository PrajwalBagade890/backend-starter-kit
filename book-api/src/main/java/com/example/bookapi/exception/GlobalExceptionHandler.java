package com.example.bookapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> notFound(ResourceNotFoundException ex, HttpServletRequest req) { return response(HttpStatus.NOT_FOUND, ex.getMessage(), req); }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String,Object>> duplicate(DuplicateResourceException ex, HttpServletRequest req) { return response(HttpStatus.CONFLICT, ex.getMessage(), req); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String,String> errors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) errors.put(error.getField(), error.getDefaultMessage());
        Map<String,Object> body = base(HttpStatus.BAD_REQUEST, "Validation failed", req); body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Map<String,Object>> integrity(Exception ex, HttpServletRequest req) { return response(HttpStatus.CONFLICT, "The request conflicts with existing data", req); }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,Object>> badJson(HttpMessageNotReadableException ex, HttpServletRequest req) { return response(HttpStatus.BAD_REQUEST, "Request body is missing or contains invalid JSON", req); }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> unexpected(Exception ex, HttpServletRequest req) { return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req); }
    private ResponseEntity<Map<String,Object>> response(HttpStatus status, String message, HttpServletRequest req) { return ResponseEntity.status(status).body(base(status, message, req)); }
    private Map<String,Object> base(HttpStatus status, String message, HttpServletRequest req) {
        Map<String,Object> body = new LinkedHashMap<>(); body.put("timestamp", LocalDateTime.now()); body.put("status", status.value()); body.put("error", status.getReasonPhrase()); body.put("message", message); body.put("path", req.getRequestURI()); return body;
    }
}
