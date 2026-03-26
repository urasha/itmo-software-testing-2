package org.example.csv;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CsvWriter")
class CsvWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void writesHeaderAndFormattedRows() throws IOException {
        Path file = tempDir.resolve("result.csv");

        CsvWriter.write(file.toString(), x -> x * x, 0.0, 0.4, 0.2);

        List<String> lines = Files.readAllLines(file);
        assertEquals(4, lines.size());
        assertEquals("X;Result", lines.get(0));
        assertEquals(formattedRow(0.0, 0.0), lines.get(1));
        assertEquals(formattedRow(0.2, 0.04), lines.get(2));
        assertEquals(formattedRow(0.4, 0.16), lines.get(3));
    }

    @Test
    void writesCustomPoints() throws IOException {
        Path file = tempDir.resolve("points.csv");

        CsvWriter.writePoints(file.toString(), x -> x + 1.0, new double[]{0.0, 1.5});

        List<String> lines = Files.readAllLines(file);
        assertEquals(3, lines.size());
        assertEquals("X;Result", lines.get(0));
        assertEquals(formattedRow(0.0, 1.0), lines.get(1));
        assertEquals(formattedRow(1.5, 2.5), lines.get(2));
    }

    @Test
    void throwsWhenStepIsNonPositive() {
        Path file = tempDir.resolve("bad_step.csv");

        assertThrows(IllegalArgumentException.class,
                () -> CsvWriter.write(file.toString(), x -> x, 0.0, 1.0, 0.0));
    }

    @Test
    void throwsWhenFromGreaterThanTo() {
        Path file = tempDir.resolve("bad_bounds.csv");

        assertThrows(IllegalArgumentException.class,
                () -> CsvWriter.write(file.toString(), x -> x, 1.0, 0.0, 0.1));
    }

    private String formattedRow(double x, double result) {
        return String.format(Locale.getDefault(), "%.6f;%.10f", x, result);
    }
}
