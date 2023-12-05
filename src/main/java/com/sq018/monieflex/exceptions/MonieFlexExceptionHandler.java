package com.sq018.monieflex.exceptions;

import com.sq018.monieflex.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class MonieFlexExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MonieFlexException.class)
    public ResponseEntity<?> handleMonieFlexException(MonieFlexException exception){
        ApiResponse<String> response = new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(response, response.getStatus());
    }
}
