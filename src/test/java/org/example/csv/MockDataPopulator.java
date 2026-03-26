package org.example.csv;

import org.example.functions.LnFunction;
import org.example.functions.LogFunction;
import org.example.functions.TanFunction;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.function.BiConsumer;

public class MockDataPopulator {
    private static final double MATCH_PRECISION = 1e-4;

    private MockDataPopulator() {}

    public static TanFunction getTanMock(Path csvPath) throws IOException {
        TanFunction mock = Mockito.mock(TanFunction.class);
        Mockito.doAnswer(invocation -> {
            throw new IllegalArgumentException("No CSV reference value for TanFunction x=" + invocation.getArgument(0));
        }).when(mock).calculate(Mockito.anyDouble());

        processCsv(csvPath, (x, y) -> Mockito.doReturn(y).when(mock).calculate(AdditionalMatchers.eq(x, MATCH_PRECISION)));
        return mock;
    }

    public static LnFunction getLnMock(Path csvPath) throws IOException {
        LnFunction mock = Mockito.mock(LnFunction.class);
        Mockito.doAnswer(invocation -> {
            throw new IllegalArgumentException("No CSV reference value for LnFunction x=" + invocation.getArgument(0));
        }).when(mock).calculate(Mockito.anyDouble());

        processCsv(csvPath, (x, y) -> Mockito.doReturn(y).when(mock).calculate(AdditionalMatchers.eq(x, MATCH_PRECISION)));
        return mock;
    }

    public static LogFunction getLogMock(Path csvPath) throws IOException {
        LogFunction mock = Mockito.mock(LogFunction.class);
        Mockito.doAnswer(invocation -> {
            throw new IllegalArgumentException("No CSV reference value for LogFunction x=" + invocation.getArgument(0));
        }).when(mock).calculate(Mockito.anyDouble());

        processCsv(csvPath, (x, y) -> Mockito.doReturn(y).when(mock).calculate(AdditionalMatchers.eq(x, MATCH_PRECISION)));
        return mock;
    }

    private static void processCsv(Path csvPath, BiConsumer<Double, Double> consumer) throws IOException {
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
                    consumer.accept(x, y);
                }
            }
        }
    }

    private static double parseDouble(String text) {
        return Double.parseDouble(text.trim().replace(',', '.'));
    }
}