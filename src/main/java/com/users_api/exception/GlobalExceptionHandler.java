package com.users_api.exception;

import com.users_api.dto.ErrorDto;
import com.users_api.response.ErrorsResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorsResponse> handleValidationExceptions(ConstraintViolationException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ex.getConstraintViolations().stream().forEach(violation -> {
            String message = violation.getMessage();
            errors.add(ErrorDto.builder().status("400").detail(message).build());
        });
        ErrorsResponse errorsResponse = ErrorsResponse.builder().errors(errors).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorsResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().stream().forEach(violation -> {
            String message = violation.getDefaultMessage();
            errors.add(ErrorDto.builder().status("400").detail(message).build());
        });
        ErrorsResponse errorsResponse = ErrorsResponse.builder().errors(errors).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorsResponse> handleValidationExceptions(NoSuchElementException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        errors.add(ErrorDto.builder().status("400").detail(ex.getMessage()).build());
        ErrorsResponse errorsResponse = ErrorsResponse.builder().errors(errors).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorsResponse> handleCommonException(Exception ex) {
        List<ErrorDto> errors = new ArrayList<>();
        errors.add(ErrorDto.builder()
                .status("400")
                .detail(ex.getMessage())
                .build());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorsResponse.builder()
                .errors(errors)
                .build());
    }
}
