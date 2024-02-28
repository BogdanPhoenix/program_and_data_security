package org.example.laboratory3;

import org.example.interfaces.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleRearrangementTest {
    private static Password passwordEncoder;

    @BeforeAll
    static void init() {
        passwordEncoder = new SimpleRearrangement();
    }

    @Test
    @DisplayName("Checks the password encryption method using a simple permutation, with the default key.")
    void testEncoderDefaultSetting() {
        var expected = "INWDILWLLDOSHW##O#UD#";
        var actual = passwordEncoder.encoder("Wild winds howl loud");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checks the password encryption method using a simple permutation with the specified symbolic key.")
    void testEncoderWithCustomKey() {
        var passwordEncoderWithKey = new SimpleRearrangement("Cit3y");
        var actual = passwordEncoderWithKey.encoder("Quick fox jumps high");
        var expected = "QIUCFKOJXUPMSIHG#H";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify the password decryption method using a simple permutation with the default key.")
    void testDecoderDefaultSetting() {
        var expected = "WILDWINDSHOWLLOUD";
        var actual = passwordEncoder.decoder("INWDILWLLDOSHW##O#UD#");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check the password decryption method using a simple permutation with a given character key.")
    void testDecoderWithCustomKey() {
        var passwordEncoderWithKey = new SimpleRearrangement("Cit3y");
        var actual = passwordEncoderWithKey.decoder("QIUCFKOJXUPMSIHG#H");
        var expected = "QUICKFOXJUMPSHIGH";
        assertEquals(expected, actual);
    }
}
