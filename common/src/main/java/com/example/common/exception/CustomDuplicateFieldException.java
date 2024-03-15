package com.example.common.exception;

public class CustomDuplicateFieldException extends RuntimeException{
    public CustomDuplicateFieldException() {
        super();
    }

    public CustomDuplicateFieldException(String message) {
        super(message);
    }

    public CustomDuplicateFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomDuplicateFieldException(Throwable cause) {
        super(cause);
    }
}
