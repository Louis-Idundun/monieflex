package com.sq018.monieflex.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.utils.UserUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class MonieFlexExceptionHandler extends ResponseEntityExceptionHandler {
    private final UserUtil userUtil;

    @ExceptionHandler(MonieFlexException.class)
    public ApiResponse<String> handleMonieFlexException(MonieFlexException exception){
        return new ApiResponse<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PaymentException.class)
    public ApiResponse<String> handlePaymentException(PaymentException exception){
        userUtil.updateWalletBalance(exception.getAmount(), false);
        userUtil.updateTransaction(exception.getTransaction());
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
}
