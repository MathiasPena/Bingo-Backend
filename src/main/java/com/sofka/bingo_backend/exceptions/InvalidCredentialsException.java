package com.sofka.bingo_backend.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException() {
        super("Invalid credentials provided");
    }
}
