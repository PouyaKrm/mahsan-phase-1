package org.example.library.dto;

import org.example.constansts.SEARCH_OPERATION;
import org.example.constansts.SearchField;

public record SearchDTO(SearchField field, String value, SEARCH_OPERATION operation) {
}
