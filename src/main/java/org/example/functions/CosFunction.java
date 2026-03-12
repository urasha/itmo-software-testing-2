package org.example.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CosFunction {

    private final SinFunction sinFunction;

    public double calculate(double x) {
        return sinFunction.calculate(Math.PI / 2 - x);
    }
}
