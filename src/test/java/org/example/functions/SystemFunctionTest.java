package org.example.functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SystemFunction")
class SystemFunctionTest {
    private static final double PI = 3.141592653589793;
    private static final double PI_2 = PI / 2.0;
    private static final double E = 2.718281828459045;
    private static final double LN_10 = 2.302585092994046;
    private static final double LOG5_10 = 1.430676558073393;
    private static final double LOG3_10 = 2.095903274289385;
    private static final double LOG5_E = 0.621334934559612;
    private static final double LOG3_E = 0.910239226626837;
    private static final double LOG10_E = 0.434294481903252;

    @Mock
    private TanFunction tanFunction;
    @Mock
    private LnFunction lnFunction;
    @Mock
    private LogFunction log5Function;
    @Mock
    private LogFunction log3Function;
    @Mock
    private LogFunction log10Function;
    private SystemFunction systemFunction;

    @BeforeEach
    void setUp() {
        systemFunction = new SystemFunction(tanFunction, lnFunction, log5Function, log3Function, log10Function);
    }

    @ParameterizedTest(name = "f({0}) = tan^3({0}), tan = {1}")
    @CsvSource({
            "0.0,               0.0",
            "-0.7853981633974,  -1.0",
            "-0.5235987755983,  -0.577350269189626",
            "-2.3561944901923,   1.0",
            "-1.0,              -1.557407724654902",
            "-2.0,               2.185039863261519",
    })
    void negativeBranch(double x, double tanVal) {
        when(tanFunction.calculate(x)).thenReturn(tanVal);
        double expected = tanVal * tanVal * tanVal;
        assertEquals(expected, systemFunction.calculate(x), 1e-6);
    }

    @Test
    @DisplayName("f(-pi/2) = NaN (асимптота)")
    void atMinusPiOver2() {
        when(tanFunction.calculate(-PI_2)).thenReturn(Double.NaN);
        assertTrue(Double.isNaN(systemFunction.calculate(-PI_2)));
    }

    @Test
    @DisplayName("f(1) = NaN (log10(1) = 0, деление на ноль)")
    void atOne() {
        when(lnFunction.calculate(1.0)).thenReturn(0.0);
        when(log5Function.calculate(1.0)).thenReturn(0.0);
        when(log3Function.calculate(1.0)).thenReturn(0.0);
        when(log10Function.calculate(1.0)).thenReturn(0.0);
        assertTrue(Double.isNaN(systemFunction.calculate(1.0)));
    }

    @Test
    @DisplayName("f(10) = ((ln^3(10) - 1)^3 + log5(10)*log3(10)) / log10(10)")
    void atTen() {
        when(lnFunction.calculate(10.0)).thenReturn(LN_10);
        when(log5Function.calculate(10.0)).thenReturn(LOG5_10);
        when(log3Function.calculate(10.0)).thenReturn(LOG3_10);
        when(log10Function.calculate(10.0)).thenReturn(1.0);
        assertEquals(1410.966236, systemFunction.calculate(10.0), 0.01);
    }

    @Test
    @DisplayName("f(e) = (0 + log5(e)*log3(e)) / log10(e)")
    void atE() {
        when(lnFunction.calculate(E)).thenReturn(1.0);
        when(log5Function.calculate(E)).thenReturn(LOG5_E);
        when(log3Function.calculate(E)).thenReturn(LOG3_E);
        when(log10Function.calculate(E)).thenReturn(LOG10_E);
        assertEquals(1.302257924, systemFunction.calculate(E), 1e-4);
    }
}
