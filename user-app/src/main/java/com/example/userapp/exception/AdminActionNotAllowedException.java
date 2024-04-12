package com.example.userapp.exception;

public class AdminActionNotAllowedException extends AdminActionException {
    public AdminActionNotAllowedException(String message) {
        super(message);
    }
}
