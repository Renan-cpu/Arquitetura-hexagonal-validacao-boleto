package com.juros.jurosboleto.adapter.http.exception;

import com.juros.jurosboleto.core.exception.ApplicationExeption;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationExceptionHandle extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationExeption.class)
     public ResponseEntity<ErrorResponse> applicationException(ApplicationExeption ex, WebRequest request) {
        var response = ErrorResponse.builder()
                .mensagens(Arrays.asList(ex.getTipoExecao().getMessage()))
                .erro(ex.getTipoExecao().toString().toLowerCase())
                .codigo(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
     }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getFieldErrors().stream()
                .map(item -> item.getField() + ": " + item.getDefaultMessage())
                .collect(Collectors.toList());

        var response = ErrorResponse.builder()
                .mensagens(errors)
                .erro("error na validacao dto")
                .codigo(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }
}
