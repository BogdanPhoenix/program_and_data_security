package org.example.laboratory3;

import org.example.interfaces.Password;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class SimpleRearrangement implements Password {
    private static final String COMPLEMENT_SYMBOL = "#";
    private static final String SPACE_TEMPLATE = "\\s+";
    private static final String SPACE_REPLACEMENT_TEMPLATE = "";

    private final int key;
    private Integer[] sequencesShuffledKey;

    public SimpleRearrangement() {
        this(146215);
    }

    public SimpleRearrangement(String keyWord) {
        this(keyWord.hashCode());
    }

    public SimpleRearrangement(int key) {
        this.key = key;
    }

    @Override
    public String encoder(String password) {
        return work(password, this::encrypt);
    }

    @Override
    public String decoder(String encryptedPassword) {
        var value = work(encryptedPassword, this::decrypt);
        return clearValue(value);
    }

    private String work(String value, UnaryOperator<String> method) {
        value = value
                .replaceAll(SPACE_TEMPLATE, SPACE_REPLACEMENT_TEMPLATE)
                .toUpperCase();

        var correlationKey = createCorrelationKey(key, value);
        var fragments = divisionIntoFragments(value, correlationKey);
        var builder = new StringBuilder();

        sequenceGeneration(correlationKey);

        for(var part : fragments) {
            builder.append(method.apply(part));
        }

        return builder.toString();
    }

    private int createCorrelationKey(int key, String value) {
        value = clearValue(value);
        var result = (key % value.length()) / 2;

        if(result < 3) {
            var newKey = hashCode(value.length(), key);
            return createCorrelationKey(newKey, value);
        }

        return result;
    }

    private String clearValue(String value) {
        return value.replaceAll(COMPLEMENT_SYMBOL, SPACE_REPLACEMENT_TEMPLATE);
    }

    private String[] divisionIntoFragments(String correlationPassword, int correlationKey) {
        var numParts = (int) Math.ceil((double) correlationPassword.length() / correlationKey);
        var fragments = new String[numParts];

        for (int i = 0; i < numParts; i++) {
            var startIndex = i * correlationKey;
            var endIndex = Math.min(correlationPassword.length(), (i + 1) * correlationKey);
            var part = correlationPassword.substring(startIndex, endIndex);

            if(part.length() < correlationKey) {
                part += COMPLEMENT_SYMBOL.repeat(correlationKey - part.length());
            }

            fragments[i] = part;
        }

        return fragments;
    }

    private void sequenceGeneration(int correlationKey) {
        var sequencesKey = createArray(correlationKey);
        sequencesShuffledKey = createSequencesShuffledKey(sequencesKey);
    }

    private Integer[] createSequencesShuffledKey(Integer[] array) {
        var newArray = new Integer[array.length];
        var numbs = new ArrayList<>(Arrays.stream(array).toList());
        var arrayIndex = 0;
        var sum = numbs
                .stream()
                .reduce(0, Integer::sum);

        while (!numbs.isEmpty()) {
            var index = hashCode(sum, numbs.size()) % numbs.size();
            if (numbs.get(index) == null) {
                continue;
            }
            var element = numbs.remove(index);
            newArray[arrayIndex] = element;
            sum -= element;
            ++arrayIndex;
        }

        return newArray;
    }

    private int hashCode(int h, int length) {
        h ^= length;
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private static Integer[] createArray(int correlationKey) {
        return IntStream.rangeClosed(1, correlationKey)
                .boxed()
                .toArray(Integer[]::new);
    }

    private String encrypt(String substring) {
        var chars = substring.toCharArray();
        var newChars = new char[chars.length];

        for(int i = 0; i < newChars.length; ++i) {
            var oldIndex = sequencesShuffledKey[i] - 1;
            newChars[i] = chars[oldIndex];
        }

        return new String(newChars);
    }

    private String decrypt(String substring) {
        var chars = substring.toCharArray();
        var newChars = new char[chars.length];

        for(int i = 0; i < newChars.length; ++i) {
            var oldIndex = sequencesShuffledKey[i] - 1;
            newChars[oldIndex] = chars[i];
        }

        return new String(newChars);
    }
}
