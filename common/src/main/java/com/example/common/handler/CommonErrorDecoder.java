package com.example.common.handler;

import com.example.common.exception.UserNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CommonErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new UserNotFoundException("User not found with provided ID");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
