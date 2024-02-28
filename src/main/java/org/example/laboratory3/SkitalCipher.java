package org.example.laboratory3;

import org.example.interfaces.Password;

public class SkitalCipher implements Password {
    private static final String COMPLEMENT_SYMBOL = "#";
    private static final String SPACE_TEMPLATE = "\\s+";
    private static final String SPACE_REPLACEMENT_TEMPLATE = "";
    private final int key;

    public SkitalCipher() {
        this(6);
    }

    public SkitalCipher(int key) {
        this.key = key;
    }

    @Override
    public String encoder(String password) {
        password = password
                .replaceAll(SPACE_TEMPLATE, SPACE_REPLACEMENT_TEMPLATE)
                .toUpperCase();

        var numAddChars = password.length() % key;

        if(numAddChars > 0) {
            password += COMPLEMENT_SYMBOL.repeat(key - numAddChars);
        }

        var column = password.length() / key;
        var builder = new StringBuilder();
        var chars = password.toCharArray();

        for(int i = 0; i < column; ++i) {
            for(int j = 0; j < key; ++j) {
                builder.append(chars[i + column * j]);
            }
        }

        return builder.toString();
    }

    @Override
    public String decoder(String encryptedPassword) {
        var column = encryptedPassword.length() / key;
        var chars = encryptedPassword.toCharArray();
        var decoderChars = new char[encryptedPassword.length()];
        int index = 0;

        for(int i = 0; i < column; ++i) {
            for(int j = 0; j < key; ++j) {
                decoderChars[i + column * j] = chars[index];
                ++index;
            }
        }

        return new String(decoderChars)
                .replaceAll(COMPLEMENT_SYMBOL, SPACE_REPLACEMENT_TEMPLATE);
    }
}
