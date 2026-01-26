package com.daniel.catalog.controllers.exceptions;

import com.daniel.catalog.services.Exceptions.DatabaseException;
import com.daniel.catalog.services.Exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExecptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){

        StandardError err = new StandardError();

        err.setTimestamp(Instant.now());

        err.setStatus(HttpStatus.NOT_FOUND.value());

        err.setError("Resource Not Found");

        err.setMessage(e.getMessage());

        err.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);

    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException e, HttpServletRequest request){

        StandardError err = new StandardError();

        err.setTimestamp(Instant.now());

        err.setStatus(HttpStatus.BAD_REQUEST.value());

        err.setError("Database exception");

        err.setMessage(e.getMessage());

        err.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){

        ValidationError err = new ValidationError();

        err.setTimestamp(Instant.now());

        err.setStatus(HttpStatus.UNPROCESSABLE_CONTENT.value());

        err.setError("Validation exception");

        err.setMessage(e.getMessage());

        err.setPath(request.getRequestURI());

        for(FieldError f : e.getBindingResult().getFieldErrors()){
            err.addError(f.getField(),f.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(err);

    }

}
