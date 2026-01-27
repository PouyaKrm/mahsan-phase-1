package org.example.exception;

public class ItemNotFoundException extends BaseException{
    public ItemNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
