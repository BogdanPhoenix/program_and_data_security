package org.example.laboratory4;

import org.example.interfaces.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GamutTest {
    private static Password passwordEncoder;

    @BeforeAll
    static void init() {
        passwordEncoder = new Gamut();
    }

    @Test
    @DisplayName("Checks the password encryption method using gamma, with the default key.")
    void testEncoderDefaultSetting() {
        var expected = "Ak-|\np?BLhrxB{\n2]g/s";
        var actual = passwordEncoder.encoder("Wild winds howl loud");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check the method of decrypting the password using the default key.")
    void testDecoderDefaultSetting() {
        var expected = "Wild winds howl loud";
        var actual = passwordEncoder.decoder("Ak-|\np?BLhrxB{\n2]g/s");
        assertEquals(expected, actual);
    }
}
