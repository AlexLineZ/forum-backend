package com.example.security.handler;

import com.example.common.exception.NotFoundException;
import com.example.common.exception.UserNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class SecurityErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new UserNotFoundException("User not found with provided ID");
        }
        if (response.status() == 404) {
            return new NotFoundException("File not found with provided ID");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
