package org.example.library.dto;

import org.example.constansts.SearchOperation;
import org.example.constansts.SearchField;

public record SearchDTO(SearchField field, String value, SearchOperation operation) {
}
