package org.example.laboratory5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecompositionPrimeFactorsTest {
    @ParameterizedTest
    @MethodSource("testValuesToFindRemainder")
    @DisplayName("")
    void testFindRemainder(long num, List<Long> expected) {
        var actual = DecompositionPrimeFactors.numberPrimeFactors(num);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToFindRemainder() {
        return Stream.of(
                Arguments.of(78L, Arrays.asList(2L, 3L, 13L)),
                Arguments.of(83_006L, Arrays.asList(2L, 7L, 7L, 7L, 11L, 11L)),
                Arguments.of(609_840L, Arrays.asList(2L, 2L, 2L, 2L, 3L, 3L, 5L, 7L, 11L, 11L))
        );
    }
}
