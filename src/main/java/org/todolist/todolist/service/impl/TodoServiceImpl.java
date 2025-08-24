package org.todolist.todolist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.todolist.todolist.dto.response.TodoResponse;
import org.todolist.todolist.entity.Todo;
import org.todolist.todolist.entity.User;
import org.todolist.todolist.exceptions.customerrors.TodoNotFoundException;
import org.todolist.todolist.mapper.TodoMapper;
import org.todolist.todolist.repository.TodoRepository;
import org.todolist.todolist.repository.UserRepository;
import org.todolist.todolist.service.TodoService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TodoMapper todoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> getAllTodosByUser(UserDetails user) {
        List<Todo> resultTodos =  todoRepository.findByUserEmail(user.getUsername());
        return todoMapper.toResponseList(resultTodos);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Todo> getTodoEntityById(Long id , UserDetails user) {
        return todoRepository.findById(id)
                .filter(todo -> todo.getUser().getEmail().equals(user.getUsername()));
    }

    @Override
    public TodoResponse getTodoById(Long id, UserDetails user) {
        Todo todo = getTodoEntityById(id , user)
                .orElseThrow(() -> new TodoNotFoundException("Not found todo"));
        return todoMapper.toResponse(todo);
    }


    @Override
    public TodoResponse createTodo(String title, String description, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("something with your details"));

        Todo createdTodo = new Todo(title , description , user);
        Todo savedTodo = todoRepository.save(createdTodo);
        return todoMapper.toResponse(savedTodo);
    }

    @Override
    public TodoResponse updateTodo(String title, String description, Long id, UserDetails user) {

        Todo todo = getTodoEntityById(id , user)
                .orElseThrow(() -> new TodoNotFoundException("Not found todo"));

        if (StringUtils.hasText(title)){
            todo.setTitle(title);
        }
        if(StringUtils.hasText(description)){
            todo.setDescription(description);
        }

        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.toResponse(savedTodo);
    }

    @Override
    public void deleteTodo(Long id , UserDetails user) {
        Todo todo = getTodoEntityById(id,user)
                .orElseThrow(()-> new TodoNotFoundException("Not found todo"));
        todoRepository.delete(todo);
    }

    @Override
    public TodoResponse markAsCompleted(Long id, UserDetails user) {
        Todo todo = getTodoEntityById(id , user)
                .orElseThrow(() -> new TodoNotFoundException("Not found todo"));
        todo.setCompleted(true);
        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.toResponse(savedTodo);
    }

    @Override
    public TodoResponse markAsIncompleted(Long id, UserDetails user) {
        Todo todo = getTodoEntityById(id , user)
                .orElseThrow(()-> new TodoNotFoundException("Not found todo"));
        todo.setCompleted(false);
        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.toResponse(savedTodo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> getCompletedTodos(UserDetails userDetails) {
        List<Todo> resultTodos = todoRepository.findByUserEmailAndCompleted(userDetails.getUsername() , true);
        return todoMapper.toResponseList(resultTodos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> getInCompletedTodos(UserDetails userDetails) {
        List<Todo> resultTodos = todoRepository.findByUserEmailAndCompleted(userDetails.getUsername() , false);
        return todoMapper.toResponseList(resultTodos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> searchTodos(UserDetails userDetails , String searchingTerm) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("Todo not found or Access denied"));

        List<Todo> resultTodos = todoRepository.findByUserAndTitleContainingIgnoreCase(user , searchingTerm);
        return todoMapper.toResponseList(resultTodos);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountTodos(UserDetails user) {
        return todoRepository.countByUserEmail(user.getUsername());
    }

    @Override
    public Long getCountCompletedTodos(UserDetails user) {
        return (long) todoRepository.findByUserEmailAndCompleted(user.getUsername() , true).size();
    }
}