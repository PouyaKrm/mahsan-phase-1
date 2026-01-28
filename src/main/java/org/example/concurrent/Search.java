package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class Search extends ConcurrentMessage{

    private final String[][] searchTerms;

    public Search(String[][] searchTerms) {
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
