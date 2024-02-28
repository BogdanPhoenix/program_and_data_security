package org.example.laboratory1_2;

import org.example.interfaces.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VigenereCipherTest {
    private static Password passwordEncoder;

    @BeforeAll
    static void init() {
        passwordEncoder = new VigenereCipher();
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
                Arguments.of("Dark clouds light sun", "VETBGEGYFJPBYLVJYG"),
                Arguments.of("Wild winds howl loud", "OMNUABFHUYSPDPQLH"),
                Arguments.of("Quick fox jumps high", "IYKTOYGBLLQIKLKXL")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToEncoder")
    @DisplayName("Checking the password encryption method using the Caesar's cipher with the specified key.")
    void testEncoderWithCustomKey(String key, String password, String expected) {
        var passwordEncoderWithKey = new VigenereCipher(key);
        var actual = passwordEncoderWithKey.encoder(password);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToEncoder() {
        return Stream.of(
                Arguments.of("City", "Dark clouds light sun", "FIKIETHSFAEGIPMQWV"),
                Arguments.of("Ukraine", "Wild winds howl loud", "QSCDEVRXCYOEYPIEU"),
                Arguments.of("Test", "Quick fox jumps high", "JYAVDJGQCYEILLAZA")
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
                Arguments.of("VETBGEGYFJPBYLVJYG", "DARKCLOUDSLIGHTSUN"),
                Arguments.of("OMNUABFHUYSPDPQLH", "WILDWINDSHOWLLOUD"),
                Arguments.of("IYKTOYGBLLQIKLKXL", "QUICKFOXJUMPSHIGH")
        );
    }

    @ParameterizedTest
    @MethodSource("testValuesWithKeyToDecoder")
    @DisplayName("Checking the password decryption method using the Caesar's cipher with the specified key.")
    void testDecoderWithCustomKey(String key, String encryptedPassword, String expected) {
        var passwordEncoderWithKey = new VigenereCipher(key);
        var actual = passwordEncoderWithKey.decoder(encryptedPassword);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testValuesWithKeyToDecoder() {
        return Stream.of(
                Arguments.of("City", "FIKIETHSFAEGIPMQWV", "DARKCLOUDSLIGHTSUN"),
                Arguments.of("Ukraine", "QSCDEVRXCYOEYPIEU", "WILDWINDSHOWLLOUD"),
                Arguments.of("Test", "JYAVDJGQCYEILLAZA", "QUICKFOXJUMPSHIGH")
        );
    }
}
