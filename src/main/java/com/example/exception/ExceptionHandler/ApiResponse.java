package com.example.exception.ExceptionHandler;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Data
@ResponseBody
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String message;
    private int code;
    private LocalDateTime timestamp;

    public static ApiResponse createApiResponse(String message, int code) {
        return new ApiResponse(message, code, LocalDateTime.now());
    }
}
