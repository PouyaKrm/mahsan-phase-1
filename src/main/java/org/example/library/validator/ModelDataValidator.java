package org.example.library.validator;

import org.example.exception.InvalidInputData;

public interface ModelDataValidator {

    void validateDate(String date, String pattern) throws InvalidInputData;
}
