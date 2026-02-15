package org.example.library.model.borrow;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.ModelRepository;

import java.sql.SQLException;
import java.util.Optional;

public interface BorrowRepository extends ModelRepository<BorrowModel> {
    Optional<BorrowModel> findByBookId(Long id) throws SQLException, ItemNotFoundException;
}
