package org.example.csv;

import org.example.functions.CosFunction;
import org.example.functions.LnFunction;
import org.example.functions.LogFunction;
import org.example.functions.SinFunction;
import org.example.functions.TanFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CsvStubDataWriter {
    private static final double[] NEGATIVE_POINTS = {-0.1, -0.2, -0.5, -1.0, -2.0};
    private static final double[] POSITIVE_POINTS = {0.1, 0.2, 0.5, 2.0, 3.0, 10.0};

    private CsvStubDataWriter() {
    }

    public static void writeDefaultStubFiles() throws IOException {
        Path outputDir = Path.of("src", "test", "resources");
        Files.createDirectories(outputDir);

        SinFunction sin = new SinFunction(1e-12);
        CosFunction cos = new CosFunction(sin);
        TanFunction tan = new TanFunction(sin, cos);

        LnFunction ln = new LnFunction(1e-12);
        LogFunction log5 = new LogFunction(ln, 5);
        LogFunction log3 = new LogFunction(ln, 3);
        LogFunction log10 = new LogFunction(ln, 10);

        CsvWriter.writePoints(outputDir.resolve("tan.csv").toString(), tan::calculate, NEGATIVE_POINTS);
        CsvWriter.writePoints(outputDir.resolve("ln.csv").toString(), ln::calculate, POSITIVE_POINTS);
        CsvWriter.writePoints(outputDir.resolve("log5.csv").toString(), log5::calculate, POSITIVE_POINTS);
        CsvWriter.writePoints(outputDir.resolve("log3.csv").toString(), log3::calculate, POSITIVE_POINTS);
        CsvWriter.writePoints(outputDir.resolve("log10.csv").toString(), log10::calculate, POSITIVE_POINTS);
    }
}
