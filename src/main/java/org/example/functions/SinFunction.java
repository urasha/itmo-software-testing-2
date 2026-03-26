package org.example.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinFunction implements ScalarFunction {

    private final double epsilon;

    public SinFunction() {
        this(1e-10);
    }

    @Override
    public double calculate(double x) {
        x = reduceArgument(x);

        double term = x;
        double sum = term;
        for (int n = 1; Math.abs(term) > epsilon; n++) {
            term *= -x * x / ((2.0 * n) * (2.0 * n + 1));
            sum += term;
        }
        return sum;
    }

    private double reduceArgument(double x) {
        x = x % (2 * Math.PI);
        if (x > Math.PI) x -= 2 * Math.PI;
        if (x < -Math.PI) x += 2 * Math.PI;
        return x;
    }
}
