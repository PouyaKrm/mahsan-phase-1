package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.constansts.SearchField;
import org.example.constansts.SearchOperation;
import org.example.library.Library;
import org.example.library.dto.SearchDTO;

import java.util.Arrays;

public class SearchCommand extends LibraryCommand {

    private final String[][] searchTerms;

    public SearchCommand(Library library, String[][] searchTerms) {
        super(library);
        this.searchTerms = searchTerms;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.SEARCH;
    }

    @Override
    public void execute() {
        var searchDtos = Arrays.stream(searchTerms).map(termValue -> {
            var field = SearchField.valueOf(termValue[0]);
            var op = SearchOperation.valueOf(termValue[2]);
            return new SearchDTO(field, termValue[1], op);
        }).toList();
        var result = library.search(searchDtos);
        for (var dto : result) {
            dto.display();
        }
    }

}
