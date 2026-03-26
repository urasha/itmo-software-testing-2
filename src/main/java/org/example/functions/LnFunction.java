package org.example.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LnFunction {

    private final double epsilon;

    public LnFunction() {
        this(1e-10);
    }

    public double calculate(double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        if (x == 1.0) {
            return 0.0;
        }

        int k = 0;
        double m = x;
        while (m >= 2.0) {
            m /= 2.0;
            k++;
        }
        while (m < 0.5) {
            m *= 2.0;
            k--;
        }

        double t = (m - 1.0) / (m + 1.0);
        double t2 = t * t;
        double term = t;
        double sum = term;
        for (int n = 1; Math.abs(term) > epsilon; n++) {
            term *= t2;
            sum += term / (2.0 * n + 1);
        }
        double lnM = 2.0 * sum;

        double ln2 = computeLn2();

        return k * ln2 + lnM;
    }

    private double computeLn2() {
        double t = 1.0 / 3.0;
        double t2 = t * t;
        double term = t;
        double sum = term;
        for (int n = 1; Math.abs(term) > epsilon; n++) {
            term *= t2;
            sum += term / (2.0 * n + 1);
        }
        return 2.0 * sum;
    }
}
