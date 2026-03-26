package org.example.csv;

import org.example.functions.ScalarFunction;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class MockDataPopulator {
    private static final double MATCH_PRECISION = 1e-4;

    private MockDataPopulator() {}

    public static <T extends ScalarFunction> T getMock(Class<T> functionClass, Path csvPath) throws IOException {
        T mock = Mockito.mock(functionClass);

        Mockito.doAnswer(invocation -> {
             throw new IllegalArgumentException("No CSV reference value for x=" + invocation.getArgument(0));
        }).when(mock).calculate(Mockito.anyDouble());

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.toLowerCase(Locale.ROOT).startsWith("x")) {
                    continue;
                }

                String[] parts = trimmed.split(";");
                if (parts.length >= 2) {
                    double x = parseDouble(parts[0]);
                    double y = parseDouble(parts[1]);

                    Mockito.doReturn(y).when(mock).calculate(AdditionalMatchers.eq(x, MATCH_PRECISION));
                }
            }
        }

        return mock;
    }

    private static double parseDouble(String text) {
        return Double.parseDouble(text.trim().replace(',', '.'));
    }
}
