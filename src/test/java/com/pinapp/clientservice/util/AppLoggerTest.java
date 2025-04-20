package com.pinapp.clientservice.util;

import org.junit.jupiter.api.Test;

public class AppLoggerTest {

    private final AppLogger appLogger = new AppLogger();

    @Test
    void shouldLogInfoWithoutError() {
        appLogger.info("Test info message");
    }

    @Test
    void shouldLogDebugWithoutError() {
        appLogger.debug("Test debug message");
    }

    @Test
    void shouldLogWarnWithoutError() {
        appLogger.warn("Test warn message");
    }

    @Test
    void shouldLogErrorWithoutThrowable() {
        appLogger.error("Test error message");
    }

    @Test
    void shouldLogErrorWithThrowable() {
        Throwable ex = new RuntimeException("Simulated exception");
        appLogger.error("Test error with exception", ex);
    }
}
