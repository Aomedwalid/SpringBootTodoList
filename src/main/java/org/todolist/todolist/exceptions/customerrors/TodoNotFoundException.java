package org.todolist.todolist.exceptions.customerrors;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super("Todo not found");
    }

    public TodoNotFoundException(String message) {
        super(message);
    }
}