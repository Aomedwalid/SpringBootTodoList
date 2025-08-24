package org.todolist.todolist.mapper;

import org.springframework.stereotype.Component;
import org.todolist.todolist.dto.request.TodoCreateRequest;
import org.todolist.todolist.dto.response.TodoResponse;
import org.todolist.todolist.entity.Todo;
import org.todolist.todolist.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoMapper {
    public TodoResponse toResponse(Todo todo){
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .userId(todo.getUser().getId())
                .build();
    }

    public Todo toEntity(TodoCreateRequest request , User user ){
        return Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .user(user)
                .build();
    }

    public List<TodoResponse> toResponseList(List<Todo> todos){
        return todos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
