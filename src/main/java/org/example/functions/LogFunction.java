package org.example.functions;

public class LogFunction {

    private final LnFunction lnFunction;
    private final double base;

    public LogFunction(LnFunction lnFunction, double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Logarithm base must be positive and not equal to 1");
        }
        this.lnFunction = lnFunction;
        this.base = base;
    }

    public double calculate(double x) {
        double lnX = lnFunction.calculate(x);
        double lnBase = lnFunction.calculate(base);
        if (Double.isNaN(lnX) || Double.isNaN(lnBase) || Math.abs(lnBase) < 1e-15) {
            return Double.NaN;
        }
        return lnX / lnBase;
    }
}
