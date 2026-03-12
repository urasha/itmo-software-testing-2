package org.example;

import org.example.csv.CsvWriter;
import org.example.functions.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        SinFunction sin = new SinFunction();
        CosFunction cos = new CosFunction(sin);
        TanFunction tan = new TanFunction(sin, cos);

        LnFunction ln = new LnFunction();
        LogFunction log5 = new LogFunction(ln, 5);
        LogFunction log3 = new LogFunction(ln, 3);
        LogFunction log10 = new LogFunction(ln, 10);

        SystemFunction system = new SystemFunction(tan, ln, log5, log3, log10);

        CsvWriter.write("output_negative.csv", system::calculate, -3.0, -0.1, 0.1);
        CsvWriter.write("output_positive.csv", system::calculate, 0.1, 10.0, 0.1);
    }
}
