package org.example.library.dto.search_result;

import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.library.book.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class SearchResultMapper {

    public static final SearchResultMapper INSTANCE = Mappers.getMapper(SearchResultMapper.class);

    @Mapping(target = "bookId", source = "book.id")
    public abstract SearchResultDTO toSearchResultDTO(Book book);
    public abstract SearchResultDTO.BorrowDTO toBorrowDTO(BorrowModel borrowModel);
}
