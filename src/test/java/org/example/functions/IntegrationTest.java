package org.example.functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Интеграционные тесты - стратегия Bottom-Up")
class IntegrationTest {
    private static final double PI = 3.141592653589793;
    private static final double PI_2 = PI / 2.0;
    private static final double E = 2.718281828459045;

    private CosFunction createRealCos() {
        SinFunction realSin = new SinFunction(1e-12);
        return new CosFunction(realSin);
    }

    private TanFunction createRealTan() {
        SinFunction realSin = new SinFunction(1e-12);
        CosFunction realCos = new CosFunction(realSin);
        return new TanFunction(realSin, realCos);
    }

    private LnFunction createRealLn() {
        return new LnFunction(1e-12);
    }

    private SystemFunction createNegativeBranchSystemFunction() {
        TanFunction realTan = createRealTan();
        LnFunction mockLn = mock(LnFunction.class);
        LogFunction mockLog5 = mock(LogFunction.class);
        LogFunction mockLog3 = mock(LogFunction.class);
        LogFunction mockLog10 = mock(LogFunction.class);
        return new SystemFunction(realTan, mockLn, mockLog5, mockLog3, mockLog10);
    }

    private SystemFunction createPositiveBranchSystemFunction() {
        TanFunction mockTan = mock(TanFunction.class);
        LnFunction realLn = createRealLn();
        LogFunction realLog5 = new LogFunction(realLn, 5);
        LogFunction realLog3 = new LogFunction(realLn, 3);
        LogFunction realLog10 = new LogFunction(realLn, 10);
        return new SystemFunction(mockTan, realLn, realLog5, realLog3, realLog10);
    }

    private SystemFunction createFullSystemFunction() {
        TanFunction realTan = createRealTan();
        LnFunction realLn = createRealLn();
        LogFunction realLog5 = new LogFunction(realLn, 5);
        LogFunction realLog3 = new LogFunction(realLn, 3);
        LogFunction realLog10 = new LogFunction(realLn, 10);
        return new SystemFunction(realTan, realLn, realLog5, realLog3, realLog10);
    }

    @ParameterizedTest(name = "cos({0}) ~ {1}")
    @CsvSource({
            "0.0,                1.0,               1e-6",
            "0.5235987755983,    0.8660254037844,   1e-6",
            "0.7853981633974,    0.7071067811865,   1e-6",
            "1.0471975511966,    0.5,               1e-6",
            "1.5707963267949,    0.0,               1e-4",
            "3.1415926535898,   -1.0,               1e-6",
            "-0.7853981633974,   0.7071067811865,   1e-6",
            "-1.5707963267949,   0.0,               1e-4",
    })
    void cosWithRealSin(double x, double expected, double eps) {
        CosFunction cosFunction = createRealCos();
        assertEquals(expected, cosFunction.calculate(x), eps);
    }

    @ParameterizedTest(name = "tan({0}) ~ {1}")
    @CsvSource({
            "0.0,                0.0,               1e-6",
            "0.7853981633974,    1.0,               1e-4",
            "-0.7853981633974,  -1.0,               1e-4",
            "0.5235987755983,    0.5773502691896,   1e-4",
            "1.0471975511966,    1.7320508075689,   1e-3",
            "-1.0,              -1.5574077246549,   1e-4",
    })
    void tanWithRealSinCos(double x, double expected, double eps) {
        TanFunction tanFunction = createRealTan();
        assertEquals(expected, tanFunction.calculate(x), eps);
    }

    @Test
    @DisplayName("tan(pi/2) = NaN")
    void tanAsymptote() {
        TanFunction tanFunction = createRealTan();
        assertTrue(Double.isNaN(tanFunction.calculate(PI_2)));
    }

    @ParameterizedTest(name = "f({0}) = tan^3({0}) ~ {1}")
    @CsvSource({
            " 0.0,               0.0,                1e-6",
            "-0.7853981633974,  -1.0,                1e-4",
            "-0.5235987755983,  -0.192450089729875,  1e-4",
            "-2.3561944901923,   1.0,                1e-3",
    })
    void negativeBranchReal(double x, double expected, double eps) {
        SystemFunction systemFunction = createNegativeBranchSystemFunction();
        assertEquals(expected, systemFunction.calculate(x), eps);
    }

    @Test
    @DisplayName("f(-1) = tan^3(-1) ~ -3.77752")
    void atMinusOne() {
        SystemFunction systemFunction = createNegativeBranchSystemFunction();
        assertEquals(-3.777521747832757, systemFunction.calculate(-1.0), 1e-3);
    }

    @Test
    @DisplayName("f(-pi/2) = NaN (асимптота)")
    void asymptote() {
        SystemFunction systemFunction = createNegativeBranchSystemFunction();
        assertTrue(Double.isNaN(systemFunction.calculate(-PI_2)));
    }

    @ParameterizedTest(name = "log5({0}) ~ {1}")
    @CsvSource({
            "5.0,   1.0,  1e-4",
            "25.0,  2.0,  1e-4",
            "125.0, 3.0,  1e-4",
            "1.0,   0.0,  1e-6",
    })
    void log5WithRealLn(double x, double expected, double eps) {
        LnFunction realLn = createRealLn();
        LogFunction log5 = new LogFunction(realLn, 5);
        assertEquals(expected, log5.calculate(x), eps);
    }

    @ParameterizedTest(name = "log3({0}) ~ {1}")
    @CsvSource({
            "3.0,   1.0,  1e-4",
            "9.0,   2.0,  1e-4",
            "27.0,  3.0,  1e-4",
            "1.0,   0.0,  1e-6",
    })
    void log3WithRealLn(double x, double expected, double eps) {
        LnFunction realLn = createRealLn();
        LogFunction log3 = new LogFunction(realLn, 3);
        assertEquals(expected, log3.calculate(x), eps);
    }

    @ParameterizedTest(name = "log10({0}) ~ {1}")
    @CsvSource({
            "10.0,   1.0,  1e-4",
            "100.0,  2.0,  1e-4",
            "1000.0, 3.0,  1e-4",
            "1.0,    0.0,  1e-6",
    })
    void log10WithRealLn(double x, double expected, double eps) {
        LnFunction realLn = createRealLn();
        LogFunction log10 = new LogFunction(realLn, 10);
        assertEquals(expected, log10.calculate(x), eps);
    }

    @Test
    @DisplayName("f(1) = NaN (деление на log10(1) = 0)")
    void atOne() {
        SystemFunction systemFunction = createPositiveBranchSystemFunction();
        assertTrue(Double.isNaN(systemFunction.calculate(1.0)));
    }

    @Test
    @DisplayName("f(10) ~ 1410.966")
    void atTen() {
        SystemFunction systemFunction = createPositiveBranchSystemFunction();
        assertEquals(1410.966236358996, systemFunction.calculate(10.0), 0.01);
    }

    @Test
    @DisplayName("f(e) ~ 1.30226")
    void atE() {
        SystemFunction systemFunction = createPositiveBranchSystemFunction();
        assertEquals(1.302257923773871, systemFunction.calculate(E), 1e-3);
    }

    @Test
    @DisplayName("f(5) ~ 47.623")
    void atFive() {
        SystemFunction systemFunction = createPositiveBranchSystemFunction();
        assertEquals(47.623195322270583, systemFunction.calculate(5.0), 0.01);
    }

    @ParameterizedTest(name = "f({0}) ~ {1}")
    @CsvSource({
            " 0.0,                0.0,                1e-6",
            "-0.7853981633974,   -1.0,                1e-4",
            "-0.5235987755983,   -0.192450089729875,  1e-4",
    })
    void fullSystemNegative(double x, double expected, double eps) {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(expected, systemFunction.calculate(x), eps);
    }

    @Test
    @DisplayName("Полный тест: f(-1) ~ -3.77752")
    void fullSystemAtMinusOne() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(-3.777521747832757, systemFunction.calculate(-1.0), 1e-3);
    }

    @Test
    @DisplayName("Полный тест: f(-2) ~ 10.43225")
    void fullSystemAtMinusTwo() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(10.432252583955655, systemFunction.calculate(-2.0), 1e-3);
    }

    @Test
    @DisplayName("Полный тест: f(-pi/2) = NaN")
    void fullSystemAsymptote() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertTrue(Double.isNaN(systemFunction.calculate(-PI_2)));
    }

    @Test
    @DisplayName("Полный тест: f(1) = NaN")
    void fullSystemAtOne() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertTrue(Double.isNaN(systemFunction.calculate(1.0)));
    }

    @Test
    @DisplayName("Полный тест: f(10) ~ 1410.966")
    void fullSystemAtTen() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(1410.966236358996, systemFunction.calculate(10.0), 0.01);
    }

    @Test
    @DisplayName("Полный тест: f(e) ~ 1.302")
    void fullSystemAtE() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(1.302257923773871, systemFunction.calculate(E), 1e-3);
    }

    @Test
    @DisplayName("Полный тест: f(0.5) ~ 6.966")
    void fullSystemAtHalf() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(6.966075894512001, systemFunction.calculate(0.5), 0.01);
    }

    @Test
    @DisplayName("Полный тест: f(2) ~ -0.083")
    void fullSystemAtTwo() {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(-0.082986438977865, systemFunction.calculate(2.0), 1e-3);
    }
}
