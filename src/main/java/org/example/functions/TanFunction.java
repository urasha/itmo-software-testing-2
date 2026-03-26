package org.example.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TanFunction implements ScalarFunction {

    private final ScalarFunction sinFunction;
    private final ScalarFunction cosFunction;

    @Override
    public double calculate(double x) {
        double cosVal = cosFunction.calculate(x);
        if (Math.abs(cosVal) < 1e-10) {
            return Double.NaN;
        }
        return sinFunction.calculate(x) / cosVal;
    }
}
