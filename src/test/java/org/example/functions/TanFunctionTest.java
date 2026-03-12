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
@DisplayName("TanFunction - юнит-тесты с заглушками Sin/Cos")
class TanFunctionTest {
    private static final double PI = 3.141592653589793;
    private static final double PI_2 = PI / 2.0;
    @Mock
    private SinFunction sinFunction;
    @Mock
    private CosFunction cosFunction;
    private TanFunction tanFunction;

    @BeforeEach
    void setUp() {
        tanFunction = new TanFunction(sinFunction, cosFunction);
    }

    @ParameterizedTest(name = "tan({0}) ~ sin/cos = {1}/{2}")
    @CsvSource({
            "0.0,               0.0,              1.0",
            "0.7853981633974,    0.7071067811865,  0.7071067811865",
            "0.5235987755983,    0.5,              0.8660254037844",
            "1.0471975511966,    0.8660254037844,  0.5",
    })
    void tanValues(double x, double sinVal, double cosVal) {
        when(sinFunction.calculate(x)).thenReturn(sinVal);
        when(cosFunction.calculate(x)).thenReturn(cosVal);
        double expected = sinVal / cosVal;
        assertEquals(expected, tanFunction.calculate(x), 1e-10);
    }

    @ParameterizedTest(name = "tan({0}) ~ sin/cos = {1}/{2}")
    @CsvSource({
            "-0.7853981633974, -0.7071067811865,  0.7071067811865",
            "-0.5235987755983, -0.5,              0.8660254037844",
    })
    void tanNegative(double x, double sinVal, double cosVal) {
        when(sinFunction.calculate(x)).thenReturn(sinVal);
        when(cosFunction.calculate(x)).thenReturn(cosVal);
        double expected = sinVal / cosVal;
        assertEquals(expected, tanFunction.calculate(x), 1e-10);
    }

    @Test
    @DisplayName("tan(pi/2) -> NaN (cos = 0)")
    void tanAtPiOver2() {
        when(cosFunction.calculate(PI_2)).thenReturn(0.0);
        assertTrue(Double.isNaN(tanFunction.calculate(PI_2)));
    }

    @Test
    @DisplayName("tan(-pi/2) -> NaN (cos = 0)")
    void tanAtMinusPiOver2() {
        when(cosFunction.calculate(-PI_2)).thenReturn(0.0);
        assertTrue(Double.isNaN(tanFunction.calculate(-PI_2)));
    }
}
