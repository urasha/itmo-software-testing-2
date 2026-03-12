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
@DisplayName("LogFunction - юнит-тесты с заглушкой LnFunction")
class LogFunctionTest {
    private static final double LN_2 = 0.693147180559945;
    private static final double LN_3 = 1.098612288668110;
    private static final double LN_5 = 1.609437912434100;
    private static final double LN_10 = 2.302585092994046;
    @Mock
    private LnFunction lnFunction;

    @ParameterizedTest(name = "log5({0}) ~ {2}")
    @CsvSource({
            "5.0,    1.609437912434100,  1.0",
            "25.0,   3.218875824868201,  2.0",
            "1.0,    0.0,                0.0",
            "125.0,  4.828313736802301,  3.0",
    })
    void log5Values(double x, double lnX, double expected) {
        when(lnFunction.calculate(x)).thenReturn(lnX);
        when(lnFunction.calculate(5.0)).thenReturn(LN_5);
        LogFunction log5 = new LogFunction(lnFunction, 5);
        assertEquals(expected, log5.calculate(x), 1e-6);
    }

    @ParameterizedTest(name = "log3({0}) ~ {2}")
    @CsvSource({
            "3.0,    1.098612288668110,  1.0",
            "9.0,    2.197224577336220,  2.0",
            "27.0,   3.295836866004329,  3.0",
            "1.0,    0.0,                0.0",
    })
    void log3Values(double x, double lnX, double expected) {
        when(lnFunction.calculate(x)).thenReturn(lnX);
        when(lnFunction.calculate(3.0)).thenReturn(LN_3);
        LogFunction log3 = new LogFunction(lnFunction, 3);
        assertEquals(expected, log3.calculate(x), 1e-6);
    }

    @ParameterizedTest(name = "log10({0}) ~ {2}")
    @CsvSource({
            "10.0,    2.302585092994046,   1.0",
            "100.0,   4.605170185988092,   2.0",
            "1000.0,  6.907755278982138,   3.0",
            "1.0,     0.0,                 0.0",
    })
    void log10Values(double x, double lnX, double expected) {
        when(lnFunction.calculate(x)).thenReturn(lnX);
        when(lnFunction.calculate(10.0)).thenReturn(LN_10);
        LogFunction log10 = new LogFunction(lnFunction, 10);
        assertEquals(expected, log10.calculate(x), 1e-6);
    }

    @Test
    @DisplayName("log возвращает NaN для отрицательного x")
    void logNonPositive() {
        when(lnFunction.calculate(-5.0)).thenReturn(Double.NaN);
        when(lnFunction.calculate(5.0)).thenReturn(LN_5);
        LogFunction log5 = new LogFunction(lnFunction, 5);
        assertTrue(Double.isNaN(log5.calculate(-5.0)));
    }

    @Test
    @DisplayName("Невалидное основание -> IllegalArgumentException")
    void invalidBase() {
        assertThrows(IllegalArgumentException.class, () -> new LogFunction(lnFunction, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new LogFunction(lnFunction, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new LogFunction(lnFunction, -2.0));
    }
}
