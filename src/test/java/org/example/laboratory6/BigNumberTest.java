package org.example.laboratory6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigNumberTest {
    @ParameterizedTest
    @MethodSource("testValues")
    @DisplayName("Check for correct addition of integers.")
    void testPlusBigNumber(String firstNum, String secondNum, String expected) {
        var actual = BigNumber.plus(
                new BigNumber(firstNum),
                new BigNumber(secondNum)
        );

        assertEquals(expected, actual.getValue());
    }

    private static Stream<Arguments> testValues() {
        return Stream.of(
                Arguments.of("124", "12", "136"),
                Arguments.of("467213", "74124", "541337"),
                Arguments.of("922337203", "922337203685", "923259540888")
        );
    }
}
