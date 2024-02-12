package org.example.implementation;

import org.example.exception.BigramException;
import org.example.exception.InvalidInputException;
import org.example.exception.PairIdenticalLettersException;
import org.example.interfaces.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayfairCipherTest {
    private static Password passwordEncoder;

    @BeforeAll
    static void init() {
        passwordEncoder = new PlayfairCipher();
    }

    @ParameterizedTest
    @MethodSource("testValuesToEncoder")
    @DisplayName("Verifies the password encryption method using the Playfair cipher, with the default key.")
    void testEncoderDefaultSetting(String password, String expected) {
        var actual = passwordEncoder.encoder(password);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToEncoder() {
        return Stream.of(
                Arguments.of("Wild winds howl loud", "0FUL30GPB1YIMZPEMP4L"),
                Arguments.of("Test", "1Y1A"),
                Arguments.of("Quick fox jumps high", "UWFAPVHM0RD0NKAVIBHI")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToEncoder")
    @DisplayName("Verifies the password encryption method using the Playfair cipher with the specified key.")
    void testEncoderWithCustomKey(String keyWord, String password, String expected) {
        var passwordEncoderWithKey = new PlayfairCipher(keyWord);
        var actual = passwordEncoderWithKey.encoder(password);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToEncoder() {
        return Stream.of(
                Arguments.of("Cit3y", "Wild winds howl loud", "RARLA43LERAPPVP6MPRF"),
                Arguments.of("Uk4a1ne", "Test", "QDTV"),
                Arguments.of("Test", "Quick fox jumps high", "MYKBQCHMY9FXNQC7IKHI")
        );
    }

    @Test
    @DisplayName("Checks for an exception when the user passed an odd number of characters.")
    void testEncoderThrowBigramException() {
        var value = "Ukraine";
        assertThrows(BigramException.class, () -> passwordEncoder.encoder(value));
    }

    @Test
    @DisplayName("Check for an exception when the user passed unsupported characters in the value.")
    void testEncoderThrowInvalidInputException() {
        var value = "test# value";
        assertThrows(InvalidInputException.class, () -> passwordEncoder.encoder(value));
    }

    @Test
    @DisplayName("Checking for an exception when two identical symbols appear in one bigram.")
    void testEncoderThrowPairIdenticalLetters() {
        var value = "Tessting";
        assertThrows(PairIdenticalLettersException.class, () -> passwordEncoder.encoder(value));
    }

    @ParameterizedTest
    @MethodSource("testValuesToDecoder")
    @DisplayName("Verification of the password decryption method using the Playfair cipher, with the default key.")
    void testDecoderDefaultSetting(String encryptedPassword, String expected) {
        var actual = passwordEncoder.decoder(encryptedPassword);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToDecoder() {
        return Stream.of(
                Arguments.of("0FUL30GPB1YIMZPEMP4L", "WILD WINDS HOWL LOUD"),
                Arguments.of("1Y1A", "TEST"),
                Arguments.of("UWFAPVHM0RD0NKAVIBHI", "QUICK FOX IUMPS HIGH")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToDecoder")
    @DisplayName("Checking the method of password decryption using the Playfair cipher with the specified key.")
    void testDecoderWithCustomKey(String keyWord, String encryptedPassword, String expected) {
        var passwordEncoderWithKey = new PlayfairCipher(keyWord);
        var actual = passwordEncoderWithKey.decoder(encryptedPassword);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToDecoder() {
        return Stream.of(
                Arguments.of("Cit3y", "RARLA43LERAPPVP6MPRF", "WILD WINDS HOWL LOUD"),
                Arguments.of("Uk4a1ne", "QDTV", "TEST"),
                Arguments.of("Test", "MYKBQCHMY9FXNQC7IKHI", "QUICK FOX IUMPS HIGH")
        );
    }
}
