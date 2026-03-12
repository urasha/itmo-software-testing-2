package org.example.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.DoubleUnaryOperator;

public class CsvWriter {

    public static void write(String filePath, DoubleUnaryOperator function,
                             double from, double to, double step) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("X,Result");
            for (double x = from; x <= to + step / 2; x += step) {
                double result = function.applyAsDouble(x);
                pw.printf("%.6f,%.10f%n", x, result);
            }
        }
    }
}
