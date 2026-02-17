package org.example.library.model.borrow;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.AbstractModelRepository;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.library.ModelAbstractFactory;

import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BorrowRepositoryImpl extends AbstractModelRepository<BorrowModel> implements BorrowRepository {

    private static BorrowRepositoryImpl instance;

    private static Map<String, DBFieldMapping> borrowFieldMapping = Map.ofEntries(
            Map.entry("userId",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(BorrowTable.TABLE_NAME)
                                .dbFieldName(BorrowTable.USER_ID)
                            .definition("INT NOT NULL")
                            .fromDB((user, val) -> user.setUserId(Long.parseLong(val)))
                            .toDB(BorrowModel::getUserId)
                            .dbType(Types.INTEGER)
                            .build()
            ),
            Map.entry("bookId",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(BorrowTable.TABLE_NAME)
                            .dbFieldName(BorrowTable.BOOK_ID)
                            .definition("INT NOT NULL")
                            .fromDB((book, val) -> book.setBookId(Long.parseLong(val)))
                            .toDB(BorrowModel::getBookId)
                            .dbType(Types.INTEGER)
                            .build()
            ),
            Map.entry("borrowedAt",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(BorrowTable.TABLE_NAME)
                            .dbFieldName(BorrowTable.BORROWED_AT)
                            .definition("INT NOT NULL")
                            .fromDB((borrow, value) -> borrow.setBorrowedAtFromEpochDay(value))
                            .toDB(BorrowModel::getBorrowedAtEpochDay)
                            .dbType(Types.BIGINT)
                            .build()
            ),
            Map.entry("returnedAt",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(BorrowTable.TABLE_NAME)
                            .dbFieldName(BorrowTable.RETURNED_AT)
                            .definition("INT")
                            .fromDB((borrow, value) -> borrow.setReturnedAtFromEpochDay(value))
                            .toDB(BorrowModel::getReturnedAtEpochDay)
                            .dbType(Types.BIGINT)
                            .build()
            )
    );

    protected BorrowRepositoryImpl() {
        super(BorrowTable.TABLE_NAME, borrowFieldMapping);
    }

    public static synchronized BorrowRepository getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new BorrowRepositoryImpl();
        try {
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BorrowModel[] getAll() throws SQLException {
        return getAll(BorrowModel.class);
    }

    @Override
    public BorrowModel[] removeAll() throws SQLException {
        return removeAll(BorrowModel.class);
    }

    @Override
    public BorrowModel getOne(Long id) throws SQLException, ItemNotFoundException {
        return getOne(id, BorrowModel.class);
    }

    @Override
    public Optional<BorrowModel> findByBookId(Long id) throws SQLException, ItemNotFoundException {
        var st = connection.prepareStatement("select * from " + BorrowTable.TABLE_NAME + " where book_id = ?");
        st.setLong(1, id);
        var result = st.executeQuery();
        if (!result.next()) {
            return Optional.empty();
        }
        var f = ModelAbstractFactory.getInstance().getDefaultFactory(BorrowModel.class);
        var model = new BorrowModel();
        f.populateFromDB(model, result, getFieldMappings());
        return Optional.of(model);
    }

    @Override
    public BorrowModel findByUserIdBookId(Long userId, Long bookId) throws SQLException, ItemNotFoundException {
        var userIdField = getFieldMappingMap().get("userId");
        var bookIdField = getFieldMappingMap().get("bookId");
        var st = connection.prepareStatement(MessageFormat.format("select {0} from {1} where {2}=? and {3}=?", getAllColumnsSelectLabel(), tableName, userIdField.dbFieldName(), bookIdField.dbFieldName()));
        st.setObject(1, userId);
        st.setObject(2, bookId);
       var result = st.executeQuery();
        if (!result.next()) {
            throw  new ItemNotFoundException("book or is not borrowed");
        }
        var f = ModelAbstractFactory.getInstance().getDefaultFactory(BorrowModel.class);
        var model = new BorrowModel();
        f.populateFromDB(model, result, getFieldMappings());
        return model;
    }
}
