package org.example.exception;

public class InvalidInputData extends BaseException{
    public InvalidInputData(String message, Exception e) {
        super(message, e);
    }

    public InvalidInputData(String message) {
        super(message);
    }
}
