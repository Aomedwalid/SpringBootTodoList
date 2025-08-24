package org.todolist.todolist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserNameRequest {
    @NotBlank(message = "name should not be null")
    @NotEmpty(message = "name should not be empty")
    private String name;
}
