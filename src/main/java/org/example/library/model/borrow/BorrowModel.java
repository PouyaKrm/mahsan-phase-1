package org.example.library.model.borrow;

import lombok.Data;
import org.example.library.model.BaseModel;

@Data
public class BorrowModel extends BaseModel {

    private Long userId;
    private Long bookId;

}
