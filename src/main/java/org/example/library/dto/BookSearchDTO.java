package org.example.library.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BookSearchDTO {
    private final String title;
    private final String author;
    private final Long userId;
    private final LocalDate returnedAtBefore;
    private final LocalDate returnedAtAfter;
    private final LocalDate borrowedAtBefore;
    private final LocalDate borrowedAtAfter;
    private final Boolean isReturned;
}
