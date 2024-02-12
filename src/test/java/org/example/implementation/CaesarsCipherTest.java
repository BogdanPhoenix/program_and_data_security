package org.example.implementation;

import org.example.interfaces.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaesarsCipherTest {
    private static Password passwordEncoder;

    @BeforeAll
    static void init() {
        passwordEncoder = new CaesarsCipher();
    }

    @ParameterizedTest
    @MethodSource("testValuesToEncoder")
    @DisplayName("Checking the password encryption method using the Caesar's cipher, with the default key.")
    void testEncoderDefaultSetting(String password, String expected) {
        var actual = passwordEncoder.encoder(password);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToEncoder() {
        return Stream.of(
                Arguments.of("Hello", "Rovvy"),
                Arguments.of("Test", "Docd"),
                Arguments.of("Ukraine", "Eubksxo"),
                Arguments.of("Heavy", "Rokfi")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToEncoder")
    @DisplayName("Checking the password encryption method using the Caesar's cipher with the specified key.")
    void testEncoderWithCustomKey(int key, String password, String expected) {
        var passwordEncoderWithKey = new CaesarsCipher(key);
        var actual = passwordEncoderWithKey.encoder(password);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToEncoder() {
        return Stream.of(
                Arguments.of(13, "Hello", "Uryyb"),
                Arguments.of(5, "Test", "Yjxy"),
                Arguments.of(6, "Ukraine", "Aqxgotk"),
                Arguments.of(20, "Heavy", "Byups")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesToDecoder")
    @DisplayName("Checking the password decryption method using the Caesar's cipher, with the default key.")
    void testDecoderDefaultSetting(String encryptedPassword, String expected) {
        var actual = passwordEncoder.decoder(encryptedPassword);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesToDecoder() {
        return Stream.of(
                Arguments.of("Rovvy", "Hello"),
                Arguments.of("Docd", "Test"),
                Arguments.of("Eubksxo", "Ukraine"),
                Arguments.of("Rokfi", "Heavy")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToDecoder")
    @DisplayName("Checking the password decryption method using the Caesar's cipher with the specified key.")
    void testDecoderWithCustomKey(int key, String encryptedPassword, String expected) {
        var passwordEncoderWithKey = new CaesarsCipher(key);
        var actual = passwordEncoderWithKey.decoder(encryptedPassword);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToDecoder() {
        return Stream.of(
                Arguments.of(13, "Uryyb", "Hello"),
                Arguments.of(5, "Yjxy", "Test"),
                Arguments.of(6, "Aqxgotk", "Ukraine"),
                Arguments.of(20, "Byups", "Heavy")
        );
    }
}
