package org.example.laboratory5;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratingSimpleMultipliersTest {
    private static GeneratingSimpleMultipliers generator;

    @BeforeAll
    static void init() {
        generator = new GeneratingSimpleMultipliers();
    }

    @Test
    @DisplayName("")
    void testFirstPrime() {
        var expected = 2;
        var actual = generator.getFirstPrime();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("testValuesToEncoder")
    @DisplayName("")
    void testLastPrime(int num, int expected) {
        generator.createSeriesPrimeNums(num);
        var actual = generator.getLastPrime();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToEncoder() {
        return Stream.of(
                Arguments.of(64, 61),
                Arguments.of(123, 113),
                Arguments.of(10, 7),
                Arguments.of(834, 829),
                Arguments.of(664, 661),
                Arguments.of(111, 109),
                Arguments.of(786, 773)
        );
    }
}
