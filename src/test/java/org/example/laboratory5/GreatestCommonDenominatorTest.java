package org.example.laboratory5;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreatestCommonDenominatorTest {
    private static GreatestCommonDenominator gcd;

    @BeforeAll
    static void init() {
        gcd = new GreatestCommonDenominator();
    }
    @ParameterizedTest
    @MethodSource("testValuesToEncoder")
    @DisplayName("")
    void testLastPrime(int firstValue, int secondValue, int expected) {
        var actual = gcd.calculate(firstValue, secondValue);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToEncoder() {
        return Stream.of(
                Arguments.of(30, 18, 6),
                Arguments.of(18, 30, 6)
        );
    }
}
