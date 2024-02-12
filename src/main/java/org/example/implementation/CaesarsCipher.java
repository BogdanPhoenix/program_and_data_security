package org.example.implementation;

import org.example.interfaces.Password;

import java.util.HashMap;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class CaesarsCipher implements Password {
    private static final char UPPERCASE_START = 'A';
    private static final char LOWERCASE_START = 'a';
    private static final int MAX_LETTER = 26;
    private static final String SPACE_TEMPLATE = "\\s+";
    private static final String SPACE_REPLACEMENT_TEMPLATE = "";

    private final int key;
    private final HashMap<Integer, Integer> hashEncoder;
    private final HashMap<Integer, Integer> hashDecoder;

    public CaesarsCipher() {
        this(10);
    }

    public CaesarsCipher(int key) {
        this.key = key;
        this.hashEncoder = new HashMap<>();
        this.hashDecoder = new HashMap<>();
    }

    @Override
    public String encoder(String password) {
        return work(password, this::dataEncryption);
    }

    @Override
    public String decoder(String encryptedPassword) {
        return work(encryptedPassword, this::dataDecryption);
    }

    private String work(String value, IntUnaryOperator workingMethod) {
        return value
                .replaceAll(SPACE_TEMPLATE, SPACE_REPLACEMENT_TEMPLATE)
                .chars()
                .map(workingMethod)
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }

    private int dataEncryption(int letter) {
        var startLetter = getStartLetter(letter);

        return hashEncoder.containsKey(letter)
                ? hashEncoder.get(letter)
                : shiftLetterEncryption(letter, startLetter);
    }

    private int shiftLetterEncryption(int letter, char startLetter) {
        var shift = (letter + key - startLetter) % MAX_LETTER;
        var newLetter = startLetter + shift;

        hashEncoder.put(letter, newLetter);

        return newLetter;
    }

    private int dataDecryption(int letter) {
        var startLetter = getStartLetter(letter);
        return hashDecoder.containsKey(letter)
                ? hashDecoder.get(letter)
                : shiftLetterDecryption(letter, startLetter);
    }

    private char getStartLetter(int letter) {
        return Character.isUpperCase(letter)
                ? UPPERCASE_START
                : LOWERCASE_START;
    }

    private int shiftLetterDecryption(int letter, char startLetter) {
        var shift = letter - key;

        var newLetter = shift < startLetter
                ? shift + MAX_LETTER
                : shift;

        hashDecoder.put(letter, newLetter);

        return newLetter;
    }
}
