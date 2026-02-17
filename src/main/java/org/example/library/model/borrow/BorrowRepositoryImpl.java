package org.example.library.model.borrow;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.AbstractModelRepository;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.library.ModelAbstractFactory;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;

public class BorrowRepositoryImpl extends AbstractModelRepository<BorrowModel> implements BorrowRepository {

    private static final String TABLE_NAME = "borrows";
    private static BorrowRepositoryImpl instance;

    private static Map<String, DBFieldMapping> borrowFieldMapping = Map.ofEntries(
            Map.entry("userId",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(TABLE_NAME)
                            .dbFieldName("user_id")
                            .definition("INT NOT NULL")
                            .fromDB((user, val) -> user.setUserId(Long.parseLong(val)))
                            .toDB(BorrowModel::getUserId)
                            .dbType(Types.INTEGER)
                            .build()
            ),
            Map.entry("bookId",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(TABLE_NAME)
                            .dbFieldName("book_id")
                            .definition("INT NOT NULL")
                            .fromDB((book, val) -> book.setBookId(Long.parseLong(val)))
                            .toDB(BorrowModel::getBookId)
                            .dbType(Types.INTEGER)
                            .build()
            ),
            Map.entry("borrowedAt",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(TABLE_NAME)
                            .dbFieldName("borrowed_at")
                            .definition("INT NOT NULL")
                            .fromDB((borrow, value) -> borrow.setBorrowedAtFromEpochDay(value))
                            .toDB(BorrowModel::getBorrowedAtEpochDay)
                            .dbType(Types.BIGINT)
                            .build()
            ),
            Map.entry("returnedAt",
                    DBFieldMapping.<BorrowModel>builder()
                            .tableName(TABLE_NAME)
                            .dbFieldName("returned_at")
                            .definition("INT")
                            .fromDB((borrow, value) -> borrow.setReturnedAtFromEpochDay(value))
                            .toDB(BorrowModel::getReturnedAtEpochDay)
                            .dbType(Types.BIGINT)
                            .build()
            )
    );

    protected BorrowRepositoryImpl() {
        super(TABLE_NAME, borrowFieldMapping);
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
        var st = connection.prepareStatement("select * from " + TABLE_NAME + " where book_id = ?");
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
}
