package com.fapa.back.shared.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fapa.back.shared.dtos.StandardError;
import com.fapa.back.shared.dtos.ValidationError;
import com.fapa.back.shared.exceptions.BadRequestException;
import com.fapa.back.shared.exceptions.ConflictException;
import com.fapa.back.shared.exceptions.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> unprocessableEntityException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String error = "Bad Request";
        ValidationError validationError = new ValidationError(Instant.now(), status.value(), error, "Validação falhou", request.getRequestURI());
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(status).body(validationError);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> badRequestException(BadRequestException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String error = "Bad Request";
        if (ex.getFieldMessages().isEmpty()) {
            return ResponseEntity.status(status).body(this.generateStandardError(status, error, ex.getMessage(), request));
        }
        ValidationError validationError = new ValidationError(Instant.now(), status.value(), error, "Validação falhou", request.getRequestURI());
        ex.getFieldMessages().forEach(fieldError -> {
            validationError.addError(fieldError.getFieldName(), fieldError.getMessage());
        });
        return ResponseEntity.status(status).body(validationError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> notFoundException(NotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String error = "Not Found";
        return ResponseEntity.status(status).body(this.generateStandardError(status, error, ex.getMessage(), request));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> conflictException(ConflictException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String error = "Conflict";
        return ResponseEntity.status(status).body(this.generateStandardError(status, error, ex.getMessage(), request));
    }

    private StandardError generateStandardError(HttpStatus status, String error, String message, HttpServletRequest request) {
        return new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
    }

}
