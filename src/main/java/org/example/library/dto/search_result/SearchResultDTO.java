package org.example.library.dto.search_result;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResultDTO {
    private Long bookId;
    private String title;
    private String author;
    private String content;
    private LocalDate pubDate;
    private List<BorrowDTO> borrowDTOList = new ArrayList<>();


    @Data
    public static class BorrowDTO {
        private LocalDate borrowedAt;
        private LocalDate returnedAt;
    }
}
