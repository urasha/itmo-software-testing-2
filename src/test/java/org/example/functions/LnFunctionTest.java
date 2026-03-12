package org.example.functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LnFunction")
class LnFunctionTest {
    private final LnFunction lnFunction = new LnFunction(1e-12);

    @ParameterizedTest(name = "ln({0}) = NaN")
    @ValueSource(doubles = {0.0, -1.0, -100.0, -0.001})
    void lnNonPositive(double x) {
        assertTrue(Double.isNaN(lnFunction.calculate(x)));
    }

    @ParameterizedTest(name = "ln({0}) ~ {1}")
    @CsvSource({
            "1.0,                0.0,              1e-10",
            "2.718281828459045,  1.0,              1e-6",
            "7.389056098930650,  2.0,              1e-6",
            "0.5,               -0.6931471805599,  1e-6",
            "0.1,               -2.3025850929940,  1e-6",
            "0.01,              -4.6051701859881,  1e-6",
            "3.0,                1.0986122886681,  1e-6",
            "5.0,                1.6094379124341,  1e-6",
            "10.0,               2.3025850929940,  1e-6",
            "100.0,              4.6051701859881,  1e-6",
    })
    void lnValues(double x, double expected, double eps) {
        assertEquals(expected, lnFunction.calculate(x), eps);
    }
}
