package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class SearchMessage extends ConcurrentMessage{

    private final String[][] searchTerms;

    public SearchMessage(String[][] searchTerms) {
        this.searchTerms = searchTerms;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.SEARCH;
    }

    public String[][] getSearchTerms() {
        return searchTerms;
    }
}
