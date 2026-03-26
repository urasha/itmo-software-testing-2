package org.example.functions;

import org.example.csv.CsvStubDataWriter;
import org.example.csv.MockDataPopulator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Top-Down Integration")
class IntegrationTest {
    private static final double TOLERANCE = 1e-3;
    private static final double[] NEGATIVE_POINTS = {-0.1, -0.2, -0.5, -1.0, -2.0};
    private static final double[] POSITIVE_POINTS = {0.1, 0.2, 0.5, 2.0, 3.0, 10.0};

    @BeforeAll
    static void generateStubData() throws Exception {
        CsvStubDataWriter.writeDefaultStubFiles();
    }

    @Test
    void trigStage0AllStubs() throws Exception {
        FunctionMocksBench m = createFunctionMocksBench();
        SystemFunction system = new SystemFunction(m.tanStub, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
        assertNegativeBranchMatches(system, m.tanStub);
    }

    @Test
    void trigStage1RealTanRestStubs() throws Exception {
        FunctionMocksBench m = createFunctionMocksBench();
        TanFunction tanReal = createRealTan();
        SystemFunction system = new SystemFunction(tanReal, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
        assertNegativeBranchMatches(system, tanReal);
    }

    @Test
    void logStage0AllStubs() throws Exception {
        FunctionMocksBench m = createFunctionMocksBench();
        SystemFunction system = new SystemFunction(m.tanStub, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
        assertPositiveBranchMatches(system, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
    }

    @Test
    void logStage1RealLnRestStubs() throws Exception {
        FunctionMocksBench m = createFunctionMocksBench();
        LnFunction lnReal = createRealLn();
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, m.log5Stub, m.log3Stub, m.log10Stub);
        assertPositiveBranchMatches(system, lnReal, m.log5Stub, m.log3Stub, m.log10Stub);
    }

    @Test
    void logStage2RealLnLog5RestStubs() throws Exception {
        FunctionMocksBench m = createFunctionMocksBench();
        LnFunction lnReal = createRealLn();
        LogFunction log5Real = new LogFunction(lnReal, 5);
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, log5Real, m.log3Stub, m.log10Stub);
        assertPositiveBranchMatches(system, lnReal, log5Real, m.log3Stub, m.log10Stub);
    }

    @Test
    void logStage3RealLnLog5Log3RestStubs() throws Exception {
        FunctionMocksBench m = createFunctionMocksBench();
        LnFunction lnReal = createRealLn();
        LogFunction log5Real = new LogFunction(lnReal, 5);
        LogFunction log3Real = new LogFunction(lnReal, 3);
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, log5Real, log3Real, m.log10Stub);
        assertPositiveBranchMatches(system, lnReal, log5Real, log3Real, m.log10Stub);
    }

    @Test
    void logStage4RealLogsTrigStubs() throws Exception {
        FunctionMocksBench m = createFunctionMocksBench();
        LnFunction lnReal = createRealLn();
        LogFunction log5Real = new LogFunction(lnReal, 5);
        LogFunction log3Real = new LogFunction(lnReal, 3);
        LogFunction log10Real = new LogFunction(lnReal, 10);
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, log5Real, log3Real, log10Real);
        assertPositiveBranchMatches(system, lnReal, log5Real, log3Real, log10Real);
    }

    private static FunctionMocksBench createFunctionMocksBench() throws Exception {
        return new FunctionMocksBench(
                MockDataPopulator.getTanMock(Path.of("src", "test", "resources", "tan.csv")),
                MockDataPopulator.getLnMock(Path.of("src", "test", "resources", "ln.csv")),
                MockDataPopulator.getLogMock(Path.of("src", "test", "resources", "log5.csv")),
                MockDataPopulator.getLogMock(Path.of("src", "test", "resources", "log3.csv")),
                MockDataPopulator.getLogMock(Path.of("src", "test", "resources", "log10.csv"))
        );
    }

    private static TanFunction createRealTan() {
        SinFunction sin = new SinFunction(1e-12);
        CosFunction cos = new CosFunction(sin);
        return new TanFunction(sin, cos);
    }

    private static LnFunction createRealLn() {
        return new LnFunction(1e-12);
    }

    private static void assertNegativeBranchMatches(SystemFunction system, TanFunction tanFn) {
        for (double x : NEGATIVE_POINTS) {
            double tanVal = tanFn.calculate(x);
            double expected = Double.isNaN(tanVal) ? Double.NaN : tanVal * tanVal * tanVal;
            double actual = system.calculate(x);
            assertClose(expected, actual);
        }
    }

    private static void assertPositiveBranchMatches(SystemFunction system,
                                                    LnFunction ln, LogFunction log5,
                                                    LogFunction log3, LogFunction log10) {
        for (double x : POSITIVE_POINTS) {
            double expected = expectedPositiveValue(ln, log5, log3, log10, x);
            double actual = system.calculate(x);
            assertClose(expected, actual);
        }
    }

    private static double expectedPositiveValue(LnFunction ln, LogFunction log5,
                                                LogFunction log3, LogFunction log10, double x) {
        double lnVal = ln.calculate(x);
        double log5Val = log5.calculate(x);
        double log3Val = log3.calculate(x);
        double log10Val = log10.calculate(x);

        if (Double.isNaN(lnVal) || Double.isNaN(log5Val) || Double.isNaN(log3Val) || Double.isNaN(log10Val)) {
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

    private static void assertClose(double expected, double actual) {
        if (Double.isNaN(expected)) {
            assertTrue(Double.isNaN(actual));
        } else {
            assertEquals(expected, actual, TOLERANCE);
        }
    }

    private record FunctionMocksBench(TanFunction tanStub, LnFunction lnStub,
                                      LogFunction log5Stub, LogFunction log3Stub, LogFunction log10Stub) {
    }
}
