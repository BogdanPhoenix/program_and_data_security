package org.example.implementation;

import org.example.interfaces.Password;

import java.util.HashMap;
import java.util.function.IntBinaryOperator;

public class VigenereCipher implements Password {
    private static final char START_LETTER = 'A';
    private static final int MAX_LETTER = 26;
    private static final String SPACE_TEMPLATE = "\\s+";
    private static final String SPACE_REPLACEMENT_TEMPLATE = "";

    private final String key;
    private final HashMap<Integer, Integer> hashEncoder;
    private final HashMap<Integer, Integer> hashDecoder;

    public VigenereCipher() {
        this("SECRET");
    }

    public VigenereCipher(String key) {
        this.key = key.toUpperCase();
        this.hashEncoder = new HashMap<>();
        this.hashDecoder = new HashMap<>();
    }

    @Override
    public String encoder(String password) {
        return work(password, this::letterEncryption);
    }

    @Override
    public String decoder(String encryptedPassword) {
        return work(encryptedPassword, this::letterDecryption);
    }

    private String work(String value, IntBinaryOperator workingMethod) {
        var builder = new StringBuilder();
        value = value
                .toUpperCase()
                .replaceAll(SPACE_TEMPLATE, SPACE_REPLACEMENT_TEMPLATE);

        for (int i = 0; i < value.length(); i++) {
            var letter = value.charAt(i);
            var keyIndex = i % key.length();
            var keyLetter = key.charAt(keyIndex);
            var encryptedLetter = (char) workingMethod.applyAsInt(letter, keyLetter);
            builder.append(encryptedLetter);
        }

        return builder.toString();
    }

    private int letterEncryption(int letter, int keyLetter) {
        return hashEncoder.containsKey(letter)
                ? hashEncoder.get(letter)
                : shiftRight(letter, keyLetter);
    }

    private int shiftRight(int letter, int keyLetter) {
        var shift = (letter + keyLetter - 2*START_LETTER) % MAX_LETTER;
        return START_LETTER + shift;
    }

    private int letterDecryption(int letter, int keyLetter) {
        return hashDecoder.containsKey(letter)
                ? hashDecoder.get(letter)
                : shiftLeft(letter, keyLetter);
    }

    private int shiftLeft(int letter, int keyLetter) {
        var shift = (letter - keyLetter) % MAX_LETTER;

        if(shift < 0) {
            shift += MAX_LETTER;
        }

        return START_LETTER + shift;
    }
}
