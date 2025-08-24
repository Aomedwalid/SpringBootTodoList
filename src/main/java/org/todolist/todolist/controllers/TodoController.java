package org.todolist.todolist.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.todolist.todolist.dto.request.TodoCreateRequest;
import org.todolist.todolist.dto.request.UpdateTodoRequest;
import org.todolist.todolist.dto.response.TodoResponse;
import org.todolist.todolist.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodosByUser(@AuthenticationPrincipal UserDetails user){
        List<TodoResponse> todos = todoService.getAllTodosByUser(user);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoResponse> getTodoByUser(@PathVariable Long id , @AuthenticationPrincipal UserDetails userDetails){
        TodoResponse resultTodo = todoService.getTodoById(id , userDetails);
        return ResponseEntity.ok(resultTodo);
    }

    @PostMapping()
    public ResponseEntity<TodoResponse> postTodoByUser(@Valid @RequestBody TodoCreateRequest createdTodo
            ,  @AuthenticationPrincipal UserDetails user){

        TodoResponse resultTodo = todoService.createTodo(createdTodo.getTitle() , createdTodo.getDescription() , user);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultTodo);
    }

    @PutMapping("{id}")
    public ResponseEntity<TodoResponse> updateTodoById(
            @PathVariable Long id ,
            @Valid @RequestBody UpdateTodoRequest updateTodoRequest,
            @AuthenticationPrincipal UserDetails user
    )
    {
        TodoResponse updatedTodo = todoService.updateTodo(updateTodoRequest.getTitle() ,
                updateTodoRequest.getDescription() , id , user);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TodoResponse> deleteTodoById(@PathVariable Long id ,  @AuthenticationPrincipal UserDetails user){
        todoService.deleteTodo(id , user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/complete")
    public ResponseEntity<TodoResponse> markAsCompleted(@PathVariable Long id ,  @AuthenticationPrincipal UserDetails user){
        TodoResponse markedTodo = todoService.markAsCompleted(id , user);
        return ResponseEntity.ok(markedTodo);
    }

    @PatchMapping("{id}/incomplete")
    public ResponseEntity<TodoResponse> markAsInCompleted(@PathVariable Long id ,  @AuthenticationPrincipal UserDetails user){
        TodoResponse unMarkedTodo = todoService.markAsIncompleted(id , user);
        return ResponseEntity.ok(unMarkedTodo);
    }

    @GetMapping("/complete")
    public ResponseEntity<List<TodoResponse>> getCompletedTasks( @AuthenticationPrincipal UserDetails user){
        List<TodoResponse> completedTodos = todoService.getCompletedTodos(user);
        return ResponseEntity.ok(completedTodos);
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<TodoResponse>> getNonCompledtedTasks(@AuthenticationPrincipal UserDetails user){
        List<TodoResponse> nonCompledtedTodos = todoService.getInCompletedTodos(user);
        return ResponseEntity.ok(nonCompledtedTodos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TodoResponse>> search(@RequestParam String term ,@AuthenticationPrincipal UserDetails user){
        System.out.println(term);

        List<TodoResponse> searchResult = todoService.searchTodos(user , term);
        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/stats")
    public ResponseEntity<String> getTodoStats(@AuthenticationPrincipal UserDetails user) {
        String total = todoService.getCountTodos(user).toString();
        String completed = todoService.getCountCompletedTodos(user).toString();

        return ResponseEntity.ok("Total: " + total + ", Completed: " + completed);
    }

}
