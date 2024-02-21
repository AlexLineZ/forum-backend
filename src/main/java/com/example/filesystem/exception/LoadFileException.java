package com.example.filesystem.exception;

public class LoadFileException extends RuntimeException{

    public LoadFileException() {
        super();
    }

    public LoadFileException(String message) {
        super(message);
    }

    public LoadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadFileException(Throwable cause) {
        super(cause);
    }
}
