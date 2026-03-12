package org.example.functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SinFunction - юнит-тесты")
class SinFunctionTest {
    private static final double PI = 3.141592653589793;
    private static final double TWO_PI = 2 * PI;
    private final SinFunction sinFunction = new SinFunction(1e-12);

    @Test
    @DisplayName("sin(0) = 0")
    void sinZero() {
        assertEquals(0.0, sinFunction.calculate(0.0), 1e-10);
    }

    @Test
    @DisplayName("sin(pi) ~ 0")
    void sinPi() {
        assertEquals(0.0, sinFunction.calculate(PI), 1e-6);
    }

    @Test
    @DisplayName("sin(2pi) ~ 0")
    void sinTwoPi() {
        assertEquals(0.0, sinFunction.calculate(TWO_PI), 1e-6);
    }

    @Test
    @DisplayName("sin(pi/2) = 1")
    void sinPiOver2() {
        assertEquals(1.0, sinFunction.calculate(PI / 2), 1e-6);
    }

    @Test
    @DisplayName("sin(-pi/2) = -1")
    void sinMinusPiOver2() {
        assertEquals(-1.0, sinFunction.calculate(-PI / 2), 1e-6);
    }

    @ParameterizedTest(name = "sin({0}) ~ {1}")
    @CsvSource({
            "0.5235987755983,  0.5,              1e-6",
            "0.7853981633974,  0.7071067811865,  1e-6",
            "1.0471975511966,  0.8660254037844,  1e-6",
    })
    void interiorPoints(double x, double expected, double eps) {
        assertEquals(expected, sinFunction.calculate(x), eps);
    }

    @Test
    @DisplayName("sin(3pi/4) ~ sqrt2/2")
    void sin3PiOver4() {
        assertEquals(0.7071067811865, sinFunction.calculate(2.3561944901923), 1e-6);
    }

    @Test
    @DisplayName("sin(3pi/2) = -1")
    void sin3PiOver2() {
        assertEquals(-1.0, sinFunction.calculate(4.7123889803847), 1e-6);
    }

    @ParameterizedTest(name = "sin({0}) ~ {1}")
    @CsvSource({
            "-0.5235987755983, -0.5,              1e-6",
            "-0.7853981633974, -0.7071067811865,  1e-6",
            "-3.1415926535898,  0.0,              1e-6",
    })
    void negativePoints(double x, double expected, double eps) {
        assertEquals(expected, sinFunction.calculate(x), eps);
    }

    @ParameterizedTest(name = "sin({0}) = -sin(-{0})")
    @ValueSource(doubles = {0.3, 0.7, 1.0, 1.5, 2.5})
    void sinIsOdd(double x) {
        double pos = sinFunction.calculate(x);
        double neg = sinFunction.calculate(-x);
        assertEquals(pos, -neg, 1e-10);
    }

    @ParameterizedTest(name = "sin({0}) = sin({0} + 2pi)")
    @ValueSource(doubles = {0.3, 1.0, 2.0, -1.0, -2.5})
    void sinPeriod2Pi(double x) {
        double base = sinFunction.calculate(x);
        double shifted = sinFunction.calculate(x + TWO_PI);
        assertEquals(base, shifted, 1e-6);
    }

    @ParameterizedTest(name = "sin({0}) ~ {1}")
    @CsvSource({
            " 10.0, -0.5440211108893, 1e-6",
            "-10.0,  0.5440211108893, 1e-6",
    })
    void largeValues(double x, double expected, double eps) {
        assertEquals(expected, sinFunction.calculate(x), eps);
    }
}
