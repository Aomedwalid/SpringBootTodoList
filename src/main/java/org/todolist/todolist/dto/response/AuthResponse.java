package org.todolist.todolist.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String type = "Bearer";
    private Long expiresIn;
    private Long id;
    private String email;
    private String name;
    private String message;

    public static AuthResponse success(String accessToken , Long expiresIn ,  Long id , String email , String name , String message){
        return AuthResponse.builder()
                .accessToken(accessToken)
                .type("Bearer")
                .expiresIn(expiresIn)
                .id(id)
                .email(email)
                .name(name)
                .message(message)
                .build();
    }

    public static AuthResponse failedResponse(String message){
        return AuthResponse.builder()
                .message(message)
                .build();
    }
}
