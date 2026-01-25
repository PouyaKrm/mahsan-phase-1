package org.example.constansts;

public enum ResourceType {
    BOOK(0),
    ARTICLE(1),
    MAGAZINE(2),
    REFERENCE(3),
    ;
    private final int value;

    private ResourceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
