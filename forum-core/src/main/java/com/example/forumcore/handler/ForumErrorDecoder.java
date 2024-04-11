package com.example.forumcore.handler;

import com.example.common.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ForumErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new NotFoundException("File not found with provided ID");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
