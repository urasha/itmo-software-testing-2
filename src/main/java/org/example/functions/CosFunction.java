package org.example.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CosFunction implements ScalarFunction {

    private final ScalarFunction sinFunction;

    @Override
    public double calculate(double x) {
        return sinFunction.calculate(Math.PI / 2 - x);
    }
}
