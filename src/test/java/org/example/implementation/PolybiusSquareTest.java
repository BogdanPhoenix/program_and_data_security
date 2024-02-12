package org.example.implementation;

import org.example.interfaces.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PolybiusSquareTest {
    private static Password passwordEncoder;

    @BeforeAll
    static void init() {
        passwordEncoder = new PolybiusSquare();
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
                Arguments.of("Wild winds howl loud", "4100300241000102342432413030321102"),
                Arguments.of("Test", "10033410"),
                Arguments.of("Quick fox jumps high", "3311002112223242001131043424002324")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToEncoder")
    @DisplayName("Verifies the password encryption method using the Playfair cipher with the specified key.")
    void testEncoderWithCustomKey(String keyWord, String password, String expected) {
        var passwordEncoderWithKey = new PolybiusSquare(keyWord);
        var actual = passwordEncoderWithKey.encoder(password);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToEncoder() {
        return Stream.of(
                Arguments.of("Cit3y", "Wild winds howl loud", "4301231243013012402131432323314112"),
                Arguments.of("Uk4a1ne", "Test", "41114041"),
                Arguments.of("Test", "Quick fox jumps high", "3234201021123042203423310214201314")
        );
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
                Arguments.of("4100300241000102342432413030321102", "WILDWINDSHOWLLOUD"),
                Arguments.of("10033410", "TEST"),
                Arguments.of("3311002112223242001131043424002324", "QUICKFOXIUMPSHIGH")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToDecoder")
    @DisplayName("Checking the method of password decryption using the Playfair cipher with the specified key.")
    void testDecoderWithCustomKey(String keyWord, String encryptedPassword, String expected) {
        var passwordEncoderWithKey = new PolybiusSquare(keyWord);
        var actual = passwordEncoderWithKey.decoder(encryptedPassword);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToDecoder() {
        return Stream.of(
                Arguments.of("Cit3y", "4301231243013012402131432323314112", "WILDWINDSHOWLLOUD"),
                Arguments.of("Uk4a1ne", "41114041", "TEST"),
                Arguments.of("Test", "3234201021123042203423310214201314", "QUICKFOXIUMPSHIGH")
        );
    }
}
