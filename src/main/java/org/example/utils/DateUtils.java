package org.example.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtils {
    public static LocalDate createDateEpochDay(Integer date) {
        return Instant.ofEpochSecond(date * 24 * 3600)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
