package com.tiddev.cinema.service.common.exception;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDetails> exception(CustomException ex , WebRequest request){
        ErrorDetails details = new ErrorDetails(
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details , HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InsufficientFounds.class)
    public ResponseEntity<ErrorDetails> InsufficientFounds(InsufficientFounds ex , WebRequest request){
        ErrorDetails details = new ErrorDetails(
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details , HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String , String> errors = new HashMap<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        errorList.forEach((error)->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors , HttpStatus.BAD_REQUEST);
        }
    }

