package org.todolist.todolist.exceptions.customerrors;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Email already used");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}