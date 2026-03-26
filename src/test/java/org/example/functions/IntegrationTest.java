package org.example.functions;

import org.example.csv.CsvLookupStub;
import org.example.csv.CsvStubDataWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Top-Down Integration")
class IntegrationTest {
    private static final double tolerance = 1e-3;
    private static final double[] negativePoints = {-0.1, -0.2, -0.5, -1.0, -2.0};
    private static final double[] positivePoints = {0.1, 0.2, 0.5, 2.0, 3.0, 10.0};

    @BeforeAll
    static void generateStubData() throws Exception {
        CsvStubDataWriter.writeDefaultStubFiles();
    }

    @Test
    void trigStage0AllStubs() throws Exception {
        ModuleBundle m = createModuleBundle();
        SystemFunction system = new SystemFunction(m.tanStub, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
        assertNegativeBranchMatches(system, m.tanStub);
    }

    @Test
    void trigStage1RealTanRestStubs() throws Exception {
        ModuleBundle m = createModuleBundle();
        TanFunction tanReal = createRealTan();
        SystemFunction system = new SystemFunction(tanReal, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
        assertNegativeBranchMatches(system, tanReal);
    }

    @Test
    void logStage0AllStubs() throws Exception {
        ModuleBundle m = createModuleBundle();
        SystemFunction system = new SystemFunction(m.tanStub, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
        assertPositiveBranchMatches(system, m.lnStub, m.log5Stub, m.log3Stub, m.log10Stub);
    }

    @Test
    void logStage1RealLnRestStubs() throws Exception {
        ModuleBundle m = createModuleBundle();
        LnFunction lnReal = createRealLn();
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, m.log5Stub, m.log3Stub, m.log10Stub);
        assertPositiveBranchMatches(system, lnReal, m.log5Stub, m.log3Stub, m.log10Stub);
    }

    @Test
    void logStage2RealLnLog5RestStubs() throws Exception {
        ModuleBundle m = createModuleBundle();
        LnFunction lnReal = createRealLn();
        LogFunction log5Real = new LogFunction(lnReal, 5);
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, log5Real, m.log3Stub, m.log10Stub);
        assertPositiveBranchMatches(system, lnReal, log5Real, m.log3Stub, m.log10Stub);
    }

    @Test
    void logStage3RealLnLog5Log3RestStubs() throws Exception {
        ModuleBundle m = createModuleBundle();
        LnFunction lnReal = createRealLn();
        LogFunction log5Real = new LogFunction(lnReal, 5);
        LogFunction log3Real = new LogFunction(lnReal, 3);
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, log5Real, log3Real, m.log10Stub);
        assertPositiveBranchMatches(system, lnReal, log5Real, log3Real, m.log10Stub);
    }

    @Test
    void logStage4RealLogsTrigStubs() throws Exception {
        ModuleBundle m = createModuleBundle();
        LnFunction lnReal = createRealLn();
        LogFunction log5Real = new LogFunction(lnReal, 5);
        LogFunction log3Real = new LogFunction(lnReal, 3);
        LogFunction log10Real = new LogFunction(lnReal, 10);
        SystemFunction system = new SystemFunction(m.tanStub, lnReal, log5Real, log3Real, log10Real);
        assertPositiveBranchMatches(system, lnReal, log5Real, log3Real, log10Real);
    }

    private static ModuleBundle createModuleBundle() throws Exception {
        return new ModuleBundle(
                loadStub("tan.csv"),
                loadStub("ln.csv"),
                loadStub("log5.csv"),
                loadStub("log3.csv"),
                loadStub("log10.csv")
        );
    }

    private static ScalarFunction loadStub(String fileName) throws Exception {
        return new CsvLookupStub(Path.of("src", "test", "resources", fileName));
    }

    private static TanFunction createRealTan() {
        SinFunction sin = new SinFunction(1e-12);
        CosFunction cos = new CosFunction(sin);
        return new TanFunction(sin, cos);
    }

    private static LnFunction createRealLn() {
        return new LnFunction(1e-12);
    }

    private static void assertNegativeBranchMatches(SystemFunction system, ScalarFunction tanFn) {
        for (double x : negativePoints) {
            double tanVal = tanFn.calculate(x);
            double expected = Double.isNaN(tanVal) ? Double.NaN : tanVal * tanVal * tanVal;
            double actual = system.calculate(x);
            assertClose(expected, actual);
        }
    }

    private static void assertPositiveBranchMatches(
            SystemFunction system,
            ScalarFunction ln,
            ScalarFunction log5,
            ScalarFunction log3,
            ScalarFunction log10
    ) {
        for (double x : positivePoints) {
            double expected = expectedPositiveValue(ln, log5, log3, log10, x);
            double actual = system.calculate(x);
            assertClose(expected, actual);
        }
    }

    private static double expectedPositiveValue(
            ScalarFunction ln,
            ScalarFunction log5,
            ScalarFunction log3,
            ScalarFunction log10,
            double x
    ) {
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
            assertEquals(expected, actual, tolerance);
        }
    }

    private record ModuleBundle(
            ScalarFunction tanStub,
            ScalarFunction lnStub,
            ScalarFunction log5Stub,
            ScalarFunction log3Stub,
            ScalarFunction log10Stub
    ) {
    }
}
