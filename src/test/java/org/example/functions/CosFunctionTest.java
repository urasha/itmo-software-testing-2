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
@DisplayName("CosFunction")
class CosFunctionTest {
    private static final double PI = 3.141592653589793;
    private static final double PI_2 = PI / 2.0;
    @Mock
    private SinFunction sinFunction;
    private CosFunction cosFunction;

    @BeforeEach
    void setUp() {
        cosFunction = new CosFunction(sinFunction);
    }

    @ParameterizedTest(name = "cos({0}) ~ {1}")
    @CsvSource({
            "0.0,               1.0",
            "0.5235987755983,   0.8660254037844",
            "0.7853981633974,   0.7071067811865",
            "1.0471975511966,   0.5",
            "1.5707963267949,   0.0",
            "2.3561944901923,  -0.7071067811865",
            "3.1415926535898,  -1.0",
            "3.6651914291881,  -0.8660254037844",
            "-0.5235987755983,  0.8660254037844",
            "-0.7853981633974,  0.7071067811865",
            "-1.5707963267949,  0.0",
            "-3.1415926535898, -1.0",
    })
    void cosValues(double x, double expected) {
        double argument = PI_2 - x;
        when(sinFunction.calculate(argument)).thenReturn(expected);
        assertEquals(expected, cosFunction.calculate(x), 1e-10);
        verify(sinFunction).calculate(argument);
    }
}
