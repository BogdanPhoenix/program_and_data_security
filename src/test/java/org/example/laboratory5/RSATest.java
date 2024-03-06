package org.example.laboratory5;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RSATest {
    private static RSA rsa;
    private static final int OPEN_KEY = 401;

    @BeforeAll
    static void init() {
        rsa = new RSA(151, 211);
    }

    @ParameterizedTest
    @MethodSource("testValuesToFindRemainder")
    @DisplayName("")
    void testFindRemainder(int a, int n, int k, int expected) {
        var actual = RSA.findRemainder(a, n, k);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToFindRemainder() {
        return Stream.of(
                Arguments.of(34, 745, 7, 6),
                Arguments.of(19, 6, 21, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("")
    void testEncoder(String input, String output) {
        var actual = rsa.encoder(input, OPEN_KEY);
        assertEquals(output, actual);
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("")
    void testDecoder(String output, String input) {
        var actual = rsa.decoder(input, OPEN_KEY);
        assertEquals(output, actual);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of("Bohdan", "ඏ狞䷢櫙ჽ㨼"),
                Arguments.of("Wild winds howl loud", "康僵㢏櫙ފ䒍僵㨼櫙弯ފ䷢狞䒍㢏ފ㢏狞䡮櫙")
        );
    }
}
