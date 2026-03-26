package org.example.csv;

import org.example.functions.ScalarFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CsvLookupStub implements ScalarFunction {
    private static final int scaleDigits = 6;

    private final Map<String, Double> table = new HashMap<>();

    public CsvLookupStub(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.toLowerCase(Locale.ROOT).startsWith("x")) {
                    continue;
                }

                String[] parts = trimmed.split(";");
                if (parts.length < 2) {
                    continue;
                }

                String key = keyFromText(parts[0]);
                double value = parseDouble(parts[1]);
                table.put(key, value);
            }
        }
    }

    @Override
    public double calculate(double x) {
        String key = keyFromValue(x);
        Double value = table.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Value not found for x=" + x);
        }
        return value;
    }

    private static String keyFromText(String text) {
        BigDecimal bd = new BigDecimal(normalize(text));
        bd = bd.setScale(scaleDigits, RoundingMode.HALF_UP);
        return bd.stripTrailingZeros().toPlainString();
    }

    private static String keyFromValue(double x) {
        BigDecimal bd = BigDecimal.valueOf(x).setScale(scaleDigits, RoundingMode.HALF_UP);
        return bd.stripTrailingZeros().toPlainString();
    }

    private static double parseDouble(String text) {
        return Double.parseDouble(normalize(text));
    }

    private static String normalize(String text) {
        return text.trim().replace(',', '.');
    }
}
