package org.todolist.todolist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoRequest {
    @Size(max = 255 , min = 1 , message = "Title must be more than 1 and less than 255 chars")
    private String title;

    //validating the description of the todo

    @Size(max = 1000 , message = "Description cannot be more than 1000 chars")
    private String description;
}
