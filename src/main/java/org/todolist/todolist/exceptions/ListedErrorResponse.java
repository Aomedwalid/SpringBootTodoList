package org.todolist.todolist.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ListedErrorResponse {
    private String message;
    private int status;
    private Map<String, String> errors;
    private LocalDateTime timeStamp;

    public static ListedErrorResponse errorResponse(String message ,Map<String , String> errors , int status){
        return ListedErrorResponse.builder()
                .message(message)
                .status(status)
                .errors(errors)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
