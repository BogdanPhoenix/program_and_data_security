package org.example.laboratory7;

import org.example.interfaces.HashFunction;

public class XORHashFunction implements HashFunction {
    @Override
    public String encoder(String value) {
        long hash = 0;
        long constant = 2103464369100961631L;
        char[] chars = value.toCharArray();

        for (char ch : chars) {
            hash = (hash ^ ch) * constant;
        }

        return String.valueOf(hash);
    }
}
