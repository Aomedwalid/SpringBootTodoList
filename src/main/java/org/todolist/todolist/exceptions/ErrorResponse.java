package org.todolist.todolist.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timeStamp;

    public static ErrorResponse errorResponse(String message , int status){
        return ErrorResponse.builder()
                .message(message)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
