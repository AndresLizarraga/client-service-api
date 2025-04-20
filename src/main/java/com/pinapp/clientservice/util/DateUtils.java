package com.pinapp.clientservice.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class DateUtils {
    public static LocalDate parseDate(String dateString) throws IllegalArgumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(dateString, formatter);

            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid '" + dateString + "' date format");
        }
    }
}
