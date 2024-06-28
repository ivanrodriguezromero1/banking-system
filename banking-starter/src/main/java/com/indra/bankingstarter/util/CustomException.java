package com.indra.bankingstarter.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    @Serial
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
