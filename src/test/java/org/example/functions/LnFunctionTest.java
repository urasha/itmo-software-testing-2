package org.example.functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LnFunction - юнит-тесты")
class LnFunctionTest {
    private final LnFunction lnFunction = new LnFunction(1e-12);

    @ParameterizedTest(name = "ln({0}) = NaN")
    @ValueSource(doubles = {0.0, -1.0, -100.0, -0.001})
    void lnNonPositive(double x) {
        assertTrue(Double.isNaN(lnFunction.calculate(x)));
    }

    @Test
    @DisplayName("ln(1) = 0")
    void lnOne() {
        assertEquals(0.0, lnFunction.calculate(1.0), 1e-10);
    }

    @Test
    @DisplayName("ln(e) = 1")
    void lnE() {
        assertEquals(1.0, lnFunction.calculate(2.718281828459045), 1e-6);
    }

    @Test
    @DisplayName("ln(e^2) = 2")
    void lnESquared() {
        assertEquals(2.0, lnFunction.calculate(7.389056098930650), 1e-6);
    }

    @ParameterizedTest(name = "ln({0}) ~ {1}")
    @CsvSource({
            "0.5,   -0.6931471805599,  1e-6",
            "0.1,   -2.3025850929940,  1e-6",
            "0.01,  -4.6051701859881,  1e-6",
    })
    void fractionalPoints(double x, double expected, double eps) {
        assertEquals(expected, lnFunction.calculate(x), eps);
    }

    @ParameterizedTest(name = "ln({0}) ~ {1}")
    @CsvSource({
            "3.0,    1.0986122886681,  1e-6",
            "5.0,    1.6094379124341,  1e-6",
            "10.0,   2.3025850929940,  1e-6",
            "100.0,  4.6051701859881,  1e-6",
    })
    void largePoints(double x, double expected, double eps) {
        assertEquals(expected, lnFunction.calculate(x), eps);
    }

    @ParameterizedTest(name = "ln(1/{0}) = -ln({0})")
    @ValueSource(doubles = {0.5, 2.0, 3.0, 10.0})
    void reciprocalSymmetry(double x) {
        double lnX = lnFunction.calculate(x);
        double lnReciprocal = lnFunction.calculate(1.0 / x);
        assertEquals(-lnX, lnReciprocal, 1e-6);
    }
}
