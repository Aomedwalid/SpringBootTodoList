package org.todolist.todolist.exceptions.customerrors;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Your identity is invalid");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
