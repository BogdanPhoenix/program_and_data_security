package org.example.laboratory4;

import org.example.interfaces.Password;

import java.util.Arrays;
import java.util.Comparator;

public class Gamut implements Password {
    private final String[] keys;

    public Gamut() {
        this("Ci3y", "Ukraine");
    }

    public Gamut(String... keys) {
        this.keys = keys;
    }

    @Override
    public String encoder(String password) {
        return work(password, keys);
    }

    @Override
    public String decoder(String encryptedPassword) {
        var reversKeys = Arrays.stream(keys)
                .sorted(Comparator.reverseOrder())
                .toArray(String[]::new);

        return work(encryptedPassword, reversKeys);
    }

    private String work(String value, String[] keys) {
        String result = value;

        for (String key : keys) {
            result = textGamma(result, key);
        }

        return result;
    }

    private String textGamma(String password, String key) {
        var xorString = new StringBuilder();
        key = correlationKey(password, key);

        for(int i = 0; i < password.length(); ++i) {
            char xorChar = (char) (password.charAt(i) ^ key.charAt(i));
            xorString.append(xorChar);
        }

        return xorString.toString();
    }

    private String correlationKey(String password, String key) {
        if(password.length() > key.length()) {
            var repeatCount = (password.length() / key.length()) - 1;
            var repeatResidue = password.length() % key.length();

            key += key.repeat(repeatCount);
            key += key.substring(0, repeatResidue);
        }
        else if (password.length() < key.length()) {
            key = key.substring(0, password.length());
        }

        return key;
    }
}
