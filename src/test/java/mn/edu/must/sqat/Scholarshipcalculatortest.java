package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


class ScholarshipCalculatorTest {

    private final ScholarshipCalculator calc = new ScholarshipCalculator();

    // ---- Part A: happy path ----
    @Test
    void letterGradeA() {
        assertEquals("A", calc.letterGrade(95));
    }

    @Test
    void letterGradeB() {
        assertEquals("B", calc.letterGrade(85));
    }

    @Test
    void gpaTwoGrades() {
        assertEquals(3.5, calc.gpa(new double[]{95, 85}), 1e-9);
    }

    @Test
    void scholarshipFull() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }

    @Test
    void scholarshipHalf() {
        assertEquals(50, calc.scholarship(3.1, 10, false));
    }

    // ---- Part B: letterGrade ----
    @Test
    void letterGradeC() {
        assertEquals("C", calc.letterGrade(75));
    }

    @Test
    void letterGradeD() {
        assertEquals("D", calc.letterGrade(65));
    }

    @Test
    void letterGradeF() {
        assertEquals("F", calc.letterGrade(30));
    }

    @Test
    void letterGradeNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
    }

    @Test
    void letterGradeAbove100Throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    // ---- Part B: gpa ----
    @Test
    void gpaAllGradeKinds() { // A+B+C+D+F = (4+3+2+1+0)/5
        assertEquals(2.0, calc.gpa(new double[]{95, 85, 75, 65, 30}), 1e-9);
    }

    @Test
    void gpaNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void gpaEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{}));
    }

    // ---- Part B: scholarship (one test per CFG edge) ----
    @Test
    void scholarshipNegativeGpaThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.1, 10, false));
    }

    @Test
    void scholarshipGpaAbove4Throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 10, false));
    }

    @Test
    void scholarshipNegativeCreditsThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.0, -1, false));
    }

    @Test
    void scholarshipViolationZero() {
        assertEquals(0, calc.scholarship(3.9, 16, true));
    }

    @Test
    void scholarship70() {
        assertEquals(70, calc.scholarship(3.5, 13, false));
    }

    @Test
    void scholarshipHighGpaFewCredits70() { // gpa>=3.8 but credits<15 -> falls to 70 rule
        assertEquals(70, calc.scholarship(3.9, 14, false));
    }

    @Test
    void scholarshipGpa34FewCredits50() { // gpa>=3.4 but credits<12 -> falls to 50 rule
        assertEquals(50, calc.scholarship(3.5, 10, false));
    }

    @Test
    void scholarshipLowGpaZero() {
        assertEquals(0, calc.scholarship(2.5, 16, false));
    }
        // ---- Part C: loop boundary (0 / 1 / 2+) ----
    @Test
    void gpaSingleGrade() { // loop runs once
        assertEquals(4.0, calc.gpa(new double[]{95}), 1e-9);
    }

    @Test
    void gpaManyGrades() { // loop runs 2+ times: (4+3+2+1)/4
        assertEquals(2.5, calc.gpa(new double[]{95, 85, 75, 65}), 1e-9);
    }
        // ---- Part D: boundary values (spec: 60-69.99 is D) ----
    @Test
    void letterGradeBoundary0() {
        assertEquals("F", calc.letterGrade(0));
    }

    @Test
    void letterGradeBoundary59_99() {
        assertEquals("F", calc.letterGrade(59.99));
    }

    @Test
    void letterGradeBoundary60() {
        assertEquals("D", calc.letterGrade(60));
    }

    @Test
    void letterGradeBoundary69_99() {
        assertEquals("D", calc.letterGrade(69.99));
    }

    @Test
    void letterGradeBoundary89_99() {
        assertEquals("B", calc.letterGrade(89.99));
    }

    @Test
    void letterGradeBoundary90() {
        assertEquals("A", calc.letterGrade(90));
    }

    @Test
    void letterGradeBoundary100() {
        assertEquals("A", calc.letterGrade(100));
    }
}