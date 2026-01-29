package org.example.constansts;

import java.util.Arrays;

public enum LibraryOperationType {
    FILE(1, "Import by file"),
    STDIN(2, "Insert directly"),
    SEARCH(3, "Search"),
    EXPORT(4, "Export"),
    BORROW(5, "Borrow"),
    SHOW_BORROWED(6, "Show borrowed"),
    RETURN(7, "Return book"),
    REMOVE(8, "Remove book"),
    SHOW_ALL(9, "Show all books"),
    END(10, "Exit");


    private final int value;
    private final String title;

    LibraryOperationType(int i, String title) {
        this.value = i;
        this.title = title;
    }

    public static LibraryOperationType fromValue(int i) {
        return Arrays.stream(LibraryOperationType.values()).filter(item -> item.value == i).findFirst().get();
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
