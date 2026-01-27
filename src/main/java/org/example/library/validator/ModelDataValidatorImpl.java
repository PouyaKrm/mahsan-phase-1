package org.example.library.validator;

import org.example.exception.InvalidInputData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModelDataValidatorImpl implements ModelDataValidator{

    private final static ModelDataValidator instance = new ModelDataValidatorImpl();

    private ModelDataValidatorImpl() {

    }

   @Override
    public void validateDate(String date, String dateFormat) throws InvalidInputData {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidInputData("Invalid date: " + date, e);
        }
    }

    public static ModelDataValidator getInstance() {
        return instance;
    }
}
