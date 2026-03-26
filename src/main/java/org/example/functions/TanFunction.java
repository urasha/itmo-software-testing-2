package org.example.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TanFunction {

    private final SinFunction sinFunction;
    private final CosFunction cosFunction;

    public double calculate(double x) {
        double cosVal = cosFunction.calculate(x);
        if (Math.abs(cosVal) < 1e-10) {
            return Double.NaN;
        }
        return sinFunction.calculate(x) / cosVal;
    }
}
