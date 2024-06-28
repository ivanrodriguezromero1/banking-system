package com.indra.bffservice.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public CustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                ", cause='" + getCause() + '\'' +
                '}';
    }
}
