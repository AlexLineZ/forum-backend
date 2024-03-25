package com.example.userapp.exception;

public class CouldNotVerifyEmailException extends RuntimeException{
    public CouldNotVerifyEmailException() {
        super();
    }

    public CouldNotVerifyEmailException(String message) {
        super(message);
    }

    public CouldNotVerifyEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouldNotVerifyEmailException(Throwable cause) {
        super(cause);
    }
}
