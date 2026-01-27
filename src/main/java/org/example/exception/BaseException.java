package org.example.exception;

import java.util.Optional;

public class BaseException extends Exception {
    private final String message;
    private final Optional<Exception> exception;

    public BaseException(String message, Exception exception) {
        this.message = message;
        this.exception = Optional.of(exception);
    }

    public BaseException(String message) {
        this.message = message;
        this.exception = Optional.empty();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
