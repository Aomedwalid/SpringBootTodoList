package org.todolist.todolist.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Email could not be empty")
    @Email(message = "Please provide valid email")
    private String email;

    @NotBlank(message = "Your name could not be empty")
    @Size(min = 2 , max = 50 , message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Password could not be empty")
    @Size(min = 6 , max = 100 , message = "Password must be between 6 and 100 characters")
    private String password;
}
