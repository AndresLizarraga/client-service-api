package com.pinapp.clientservice.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateUtilsTest {
    @Test
    void shouldParseValidDateCorrectly() {
        String input = "2023-10-05";
        LocalDate expected = LocalDate.of(2023, 10, 5);

        LocalDate result = DateUtils.parseDate(input);

        assertEquals(expected, result);
    }

    @Test
    void shouldThrowExceptionForInvalidFormat() {
        String invalidDate = "05-10-2023";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DateUtils.parseDate(invalidDate)
        );

        assertEquals("Invalid '05-10-2023' date format", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForGarbageString() {
        String garbage = "abc123";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DateUtils.parseDate(garbage)
        );

        assertEquals("Invalid 'abc123' date format", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForEmptyString() {
        String empty = "";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DateUtils.parseDate(empty)
        );

        assertEquals("Invalid '' date format", exception.getMessage());
    }
}
