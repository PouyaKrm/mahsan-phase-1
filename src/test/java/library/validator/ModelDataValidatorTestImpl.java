package library.validator;

import org.example.exception.InvalidInputData;
import org.example.library.validator.ModelDataValidatorImpl;
import org.junit.Test;

public class ModelDataValidatorTestImpl {
    @Test(expected = InvalidInputData.class)
    public void throws_exception_on_invalid_date() throws InvalidInputData {
        var validator = ModelDataValidatorImpl.getInstance();

        validator.validateDate("20-20-20", "dd-MM-yyyy");
    }
}
