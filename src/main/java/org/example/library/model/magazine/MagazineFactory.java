package org.example.library.model.magazine;

import org.example.exception.InvalidInputData;
import org.example.library.validator.ModelDataValidator;
import org.example.library.validator.ModelDataValidatorImpl;
import org.example.library.model.AbstractModelFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MagazineFactory extends AbstractModelFactory<Magazine> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final MagazineFactory factory = new MagazineFactory();
    private final ModelDataValidator validator = ModelDataValidatorImpl.getInstance();

    private MagazineFactory() {

    }

    public static MagazineFactory getFactory() {
        return factory;
    }

    @Override
    public Magazine createModelFromString(String string) throws InvalidInputData {
        var str = string.trim();
        String[] fields = str.split(DELIMETER);
        if (fields.length != 4) {
            throw new InvalidInputData("Invalid book line: " + str);
        }
        var dateField = fields[2];
        validator.validateDate(dateField, DATE_FORMAT);
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return new Magazine(date, fields[0], fields[1], fields[3]);
    }

    @Override
    public String parseModelToString(Magazine model) {
        return MessageFormat.format("{0},{1},{2},{3},{4}", model.getTitle(), model.getAuthor(), model.getPubDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString(), model.getContent());
    }

    @Override
    public String getDelimeter() {
        return DELIMETER;
    }
}
