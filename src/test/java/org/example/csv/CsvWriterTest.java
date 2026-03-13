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
    @DisplayName("writes header and formatted rows")
    void writesHeaderAndFormattedRows() throws IOException {
        Path file = tempDir.resolve("result.csv");

        CsvWriter.write(file.toString(), x -> x * x, 0.0, 0.4, 0.2);

        List<String> lines = Files.readAllLines(file);
        assertEquals(4, lines.size());
        assertEquals("X,Result", lines.get(0));
        assertEquals(formattedRow(0.0, 0.0), lines.get(1));
        assertEquals(formattedRow(0.2, 0.04), lines.get(2));
        assertEquals(formattedRow(0.4, 0.16), lines.get(3));
    }

    @Test
    @DisplayName("includes right bound despite floating-point error")
    void includesRightBoundDespiteFloatingPointError() throws IOException {
        Path file = tempDir.resolve("floating.csv");

        CsvWriter.write(file.toString(), x -> x, 0.0, 0.3, 0.1);

        List<String> lines = Files.readAllLines(file);
        assertEquals(5, lines.size());
        assertEquals(formattedRow(0.3, 0.3), lines.get(4));
    }

    @Test
    @DisplayName("throws when parent directory does not exist")
    void throwsWhenParentDirectoryDoesNotExist() {
        Path file = tempDir.resolve("missing").resolve("out.csv");
        DoubleUnaryOperator identity = x -> x;

        assertThrows(IOException.class,
                () -> CsvWriter.write(file.toString(), identity, 0.0, 1.0, 0.5));
    }

    @Test
    @DisplayName("applies function for each generated x")
    void appliesFunctionForEachGeneratedX() throws IOException {
        Path file = tempDir.resolve("calls.csv");
        final int[] calls = {0};

        CsvWriter.write(file.toString(), x -> {
            calls[0]++;
            return x + 1.0;
        }, 0.0, 0.2, 0.1);

        assertEquals(3, calls[0]);
        List<String> lines = Files.readAllLines(file);
        assertTrue(lines.contains(formattedRow(0.1, 1.1)));
    }

    private String formattedRow(double x, double result) {
        return String.format(Locale.getDefault(), "%.6f;%.10f", x, result);
    }
}
