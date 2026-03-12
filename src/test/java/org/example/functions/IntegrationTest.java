package org.example.functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Интеграционные тесты")
class IntegrationTest {
    private static final double PI = 3.141592653589793;
    private static final double PI_2 = PI / 2.0;

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
            "-1.0,              -3.777521747832757,  1e-3",
    })
    void negativeBranchReal(double x, double expected, double eps) {
        SystemFunction systemFunction = createNegativeBranchSystemFunction();
        assertEquals(expected, systemFunction.calculate(x), eps);
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

    @ParameterizedTest(name = "f({0}) ~ {1}")
    @CsvSource({
            "10.0,               1410.966236358996,   0.01",
            "2.718281828459045,  1.302257923773871,   1e-3",
            "5.0,                47.623195322270583,  0.01",
    })
    void positiveBranchReal(double x, double expected, double eps) {
        SystemFunction systemFunction = createPositiveBranchSystemFunction();
        assertEquals(expected, systemFunction.calculate(x), eps);
    }

    @Test
    @DisplayName("f(1) = NaN (деление на log10(1) = 0)")
    void positiveBranchRealAtOne() {
        SystemFunction systemFunction = createPositiveBranchSystemFunction();
        assertTrue(Double.isNaN(systemFunction.calculate(1.0)));
    }

    @ParameterizedTest(name = "f({0}) ~ {1}")
    @CsvSource({
            " 0.0,               0.0,                1e-6",
            "-0.7853981633974,  -1.0,                1e-4",
            "-0.5235987755983,  -0.192450089729875,  1e-4",
            "-1.0,              -3.777521747832757,  1e-3",
            "-2.0,               10.432252583955655,  1e-3",
    })
    void fullSystemNegative(double x, double expected, double eps) {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(expected, systemFunction.calculate(x), eps);
    }

    @ParameterizedTest(name = "f({0}) ~ {1}")
    @CsvSource({
            "10.0,               1410.966236358996,   0.01",
            "2.718281828459045,  1.302257923773871,   1e-3",
            "0.5,                6.966075894512001,   0.01",
            "2.0,               -0.082986438977865,   1e-3",
    })
    void fullSystemPositive(double x, double expected, double eps) {
        SystemFunction systemFunction = createFullSystemFunction();
        assertEquals(expected, systemFunction.calculate(x), eps);
    }

    @ParameterizedTest(name = "f({0}) = NaN")
    @ValueSource(doubles = {-1.5707963267949, 1.0})
    void fullSystemNaN(double x) {
        SystemFunction systemFunction = createFullSystemFunction();
        assertTrue(Double.isNaN(systemFunction.calculate(x)));
    }

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
}
