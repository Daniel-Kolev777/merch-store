package com.merchstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, Object> errorResponse = new HashMap<>();

        BindingResult bindingResult = ex.getBindingResult();

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", "Validation failed");
        errorResponse.put("errors", errors);

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(
            EntityNotFoundException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Not Found");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(EntityDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleEntityDuplicate(
            EntityDuplicateException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Conflict");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Map<String, String>> handleInvalidProduct(
            InvalidProductException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Map<String, String>> handleAuthorizationException(AuthorizationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(
            RuntimeException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(
            IOException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(
            BadCredentialsException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UserAlreadyActiveException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyActive(
            UserAlreadyActiveException ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(
            Exception ex) {

        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "Something went wrong");

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
