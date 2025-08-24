package org.todolist.todolist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "old password cannot be null")
    @NotEmpty(message = "You must enter the old password")
    private String curretPassword;

    @NotBlank(message = "Password could not be empty")
    @Size(min = 6 , max = 100 , message = "Password must be between 6 and 100 characters")
    private String updatedPassword;
}