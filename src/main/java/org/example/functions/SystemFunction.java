package org.example.functions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SystemFunction {

    private final TanFunction tanFunction;
    private final LnFunction lnFunction;
    private final LogFunction log5Function;
    private final LogFunction log3Function;
    private final LogFunction log10Function;

    public double calculate(double x) {
        if (x <= 0) {
            return calculateNegativeBranch(x);
        } else {
            return calculatePositiveBranch(x);
        }
    }

    private double calculateNegativeBranch(double x) {
        double tanVal = tanFunction.calculate(x);
        if (Double.isNaN(tanVal)) {
            return Double.NaN;
        }
        return tanVal * tanVal * tanVal;
    }

    private double calculatePositiveBranch(double x) {
        double lnVal = lnFunction.calculate(x);
        double log5Val = log5Function.calculate(x);
        double log3Val = log3Function.calculate(x);
        double log10Val = log10Function.calculate(x);

        if (Double.isNaN(lnVal) || Double.isNaN(log5Val) ||
                Double.isNaN(log3Val) || Double.isNaN(log10Val)) {
            return Double.NaN;
        }

        if (Math.abs(log10Val) < 1e-10) {
            return Double.NaN;
        }

        double lnCubed = lnVal * lnVal * lnVal;
        double inner = lnCubed - 1.0;
        double innerCubed = inner * inner * inner;
        double product = log5Val * log3Val;

        return (innerCubed + product) / log10Val;
    }
}
