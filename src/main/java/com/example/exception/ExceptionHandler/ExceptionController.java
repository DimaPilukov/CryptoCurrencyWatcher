package com.example.exception.ExceptionHandler;

import com.example.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse> handleApplicationException(ApplicationException e) {
        log.error("{} Exception caught: {}", e.getClass().getSimpleName(), e.getMessage());
        return buildResponseEntity(e.getMessage(), e.getHttpStatus());
    }

    private ResponseEntity<ApiResponse> buildResponseEntity(String message, HttpStatus httpStatus) {
        ApiResponse apiResponse = ApiResponse.createApiResponse(message, httpStatus.value());
        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

}
