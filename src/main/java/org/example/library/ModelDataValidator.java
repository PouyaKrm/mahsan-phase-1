package org.example.library;

import org.example.exception.InvalidInputData;

public interface ModelDataValidator {

    void validateDate(String date, String pattern) throws InvalidInputData;
}
