package org.todolist.todolist.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.todolist.todolist.dto.response.TodoResponse;
import org.todolist.todolist.entity.Todo;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;

public interface TodoService {

    //CRUD operations for the todo

    List<TodoResponse> getAllTodosByUser(UserDetails userDetails);
    Optional<Todo> getTodoEntityById(Long id , UserDetails userDetails);
    TodoResponse getTodoById(Long id , UserDetails userDetails);
    TodoResponse createTodo(String title , String description , UserDetails user);
    TodoResponse updateTodo(String title , String description , Long id , UserDetails user);
    void deleteTodo(Long id , UserDetails user);

    //status of the todo

    TodoResponse markAsCompleted(Long id , UserDetails user );
    TodoResponse markAsIncompleted(Long id , UserDetails user);

    // filters and search

    List<TodoResponse> getCompletedTodos(UserDetails user);
    List<TodoResponse> getInCompletedTodos(UserDetails user);
    List<TodoResponse> searchTodos(UserDetails user , String searchKey);

    //count todos

    Long getCountTodos(UserDetails user);
    Long getCountCompletedTodos(UserDetails user);
}
