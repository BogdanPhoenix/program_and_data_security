package org.example.laboratory3;

import org.example.interfaces.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkitalCipherTest {
    private static Password passwordEncoder;

    @BeforeAll
    static void init() {
        passwordEncoder = new SkitalCipher();
    }

    @Test
    @DisplayName("Checks the password encryption method using a simple permutation, with the default key.")
    void testEncoderDefaultSetting() {
        var expected = "WDNHLUIWDOLDLISWO#";
        var actual = passwordEncoder.encoder("Wild winds howl loud");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checks the password encryption method using a simple permutation with the specified symbolic key.")
    void testEncoderWithCustomKey() {
        var passwordEncoderWithKey = new SkitalCipher(8);
        var actual = passwordEncoderWithKey.encoder("Quick fox jumps high");
        var expected = "QCOUSG##UKXMHH##IFJPI###";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify the password decryption method using a simple permutation with the default key.")
    void testDecoderDefaultSetting() {
        var expected = "WILDWINDSHOWLLOUD";
        var actual = passwordEncoder.decoder("WDNHLUIWDOLDLISWO#");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check the password decryption method using a simple permutation with a given character key.")
    void testDecoderWithCustomKey() {
        var passwordEncoderWithKey = new SkitalCipher(8);
        var actual = passwordEncoderWithKey.decoder("QCOUSG##UKXMHH##IFJPI###");
        var expected = "QUICKFOXJUMPSHIGH";
        assertEquals(expected, actual);
    }
}
