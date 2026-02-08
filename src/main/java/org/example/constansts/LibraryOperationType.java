package org.example.constansts;

import java.util.Arrays;
import java.util.List;

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
    END(10, "Exit"),
    INITIALIZE(-1);


    private final int value;
    private final String title;
    private final boolean selectable;

    LibraryOperationType(int i, String title) {
        this.value = i;
        this.title = title;
        this.selectable = true;
    }

    LibraryOperationType(int i) {
        this.value = i;
        this.selectable = false;
        title = null;
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

    public boolean isSelectable() {
        return selectable;
    }

    public static List<LibraryOperationType> getSelectableOptions() {
        return Arrays.stream(LibraryOperationType.values()).filter(LibraryOperationType::isSelectable).toList();
    }
}
