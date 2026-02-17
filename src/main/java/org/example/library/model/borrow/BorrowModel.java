package org.example.library.model.borrow;

import lombok.Data;
import org.example.library.model.BaseModel;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class BorrowModel extends BaseModel {

    private Long userId;
    private Long bookId;
    private LocalDate borrowedAt;
    private LocalDate returnedAt;

    public static final String BOOK_ID_FIELD_NAME = "bookId";
    public static final String USER_ID_FIELD_NAME = "userId";
    public static final String RETURNED_AT_FIELD_NAME = "returnedAt";
    public static final String BORROWED_AT_FIELD_NAME = "borrowedAt";

    public Long getBorrowedAtEpochDay() {
        return Objects.nonNull(borrowedAt) ? borrowedAt.toEpochDay() : null;
    }

    public Long getReturnedAtEpochDay() {
        return Objects.nonNull(returnedAt) ? returnedAt.toEpochDay() : null;
    }

    public void setBorrowedAtFromEpochDay(long epochDay) {
        this.borrowedAt = LocalDate.ofEpochDay(epochDay);
    }

    public void setBorrowedAtFromEpochDay(String epochDay) {
        if(Objects.isNull(epochDay)) {
            return;
        }
        this.borrowedAt = LocalDate.ofEpochDay(Long.parseLong(epochDay));
    }

    public void setReturnedAtFromEpochDay(long epochDay) {
        this.returnedAt = LocalDate.ofEpochDay(epochDay);
    }

    public void setReturnedAtFromEpochDay(String epochDay) {
        if(Objects.isNull(epochDay)) {
            return;
        }
        this.returnedAt = LocalDate.ofEpochDay(Long.parseLong(epochDay));
    }
}
