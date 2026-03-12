package org.example.functions;

public class CosFunction {

    private final SinFunction sinFunction;

    public CosFunction(SinFunction sinFunction) {
        this.sinFunction = sinFunction;
    }

    public double calculate(double x) {
        return sinFunction.calculate(Math.PI / 2 - x);
    }
}
