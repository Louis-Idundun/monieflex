package com.sq018.monieflex.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sq018.monieflex.payloads.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class MonieFlexExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MonieFlexException.class)
    public ApiResponse<String> handleMonieFlexException(MonieFlexException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<String> handleUsernameNotFoundException(UsernameNotFoundException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LockedException.class)
    public ApiResponse<String> handleLockedException(LockedException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ApiResponse<String> handleJsonProcessingException(JsonProcessingException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MessagingException.class)
    public ApiResponse<String> handleMessagingException(MessagingException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ApiResponse<String> handleHttpClientErrorException(HttpClientErrorException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<String> handleConstraintViolationException(ConstraintViolationException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<String> handleBadCredentialsException(){
        return new ApiResponse<>(
                "Incorrect user details",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponse<String> handleExpiredJwtException(ExpiredJwtException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
