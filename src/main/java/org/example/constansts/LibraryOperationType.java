package org.example.constansts;

import java.util.Arrays;

public enum LibraryOperationType {
    FILE(1),
    STDIN(2),
    SEARCH(3),
    EXPORT(4),
    BORROW(5),
    SHOW_BORROWED(6),
    RETURN(7),
    END(8);


    private final int value;

    LibraryOperationType(int i) {
        this.value = i;
    }

    public static LibraryOperationType fromValue(int i) {
        return Arrays.stream(LibraryOperationType.values()).filter(item -> item.value == i).findFirst().get();
    }

    public int getValue() {
        return value;
    }
}
