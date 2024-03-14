package org.example.laboratory6;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class BigNumber {
    private static final int DISCHARGE = 4;
    private static final String COMPLEMENT_SYMBOL = "0";
    private final List<Integer> fragments;
    private final int size;

    public BigNumber(String num) {
        var strFragments = reverseString(num).split("(?<=\\G.{" + DISCHARGE + "})", 0);

        this.size = num.length();
        this.fragments = Arrays.stream(strFragments)
                .map(BigNumber::reverseString)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private BigNumber(List<Integer> number) {
        this.fragments = number;
        this.calculateNum();
        this.size = sizeUpdate();
    }

    private void calculateNum() {
        Deque<Integer> deque = new LinkedList<>(fragments);
        Integer value;
        var index = 0;

        while ((value = deque.pollFirst()) != null) {
            var strNum = String.valueOf(value);
            ++index;

            if(strNum.length() <= DISCHARGE) {
                continue;
            }

            acceleration(strNum, index);
        }
    }

    private void acceleration(String strNum, int index) {
        var cutNum = strNum.substring(1);

        if(index == fragments.size()) {
            fragments.add(0);
        }

        var newNextNum = fragments.get(index) + 1;
        fragments.set(index - 1, Integer.parseInt(cutNum));
        fragments.set(index, newNextNum);
    }

    private int sizeUpdate() {
        return fragments
                .stream()
                .map(String::valueOf)
                .mapToInt(String::length)
                .sum();
    }

    private static String reverseString(String str) {
        return new StringBuilder(str)
                .reverse()
                .toString();
    }

    public static BigNumber plus(BigNumber firstNum, BigNumber secondNum) {
        var longNum = longNumber(firstNum, secondNum);
        var shortNum = shortNumber(firstNum, secondNum);

        for(int i = 0; i < shortNum.size(); ++i) {
            var newValue = longNum.get(i) + shortNum.get(i);
            longNum.set(i, newValue);
        }

        return new BigNumber(longNum);
    }

    private static List<Integer> longNumber(BigNumber firstNum, BigNumber secondNum) {
        return firstNum.size >= secondNum.size
                ? firstNum.fragments
                : secondNum.fragments;
    }

    private static List<Integer> shortNumber(BigNumber firstNum, BigNumber secondNum) {
        return firstNum.size < secondNum.size
                ? firstNum.fragments
                : secondNum.fragments;
    }

    public String getValue() {
        var result = new StringBuilder();
        Deque<Integer> deque = new LinkedList<>(fragments);

        var value = deque.pollLast();
        result.append(value);

        while((value = deque.pollLast()) != null) {
            var strNum = String.valueOf(value);
            result.append(complement(strNum));
            result.append(value);
        }

        return result.toString();
    }

    private String complement(String value) {
        if(value.length() < DISCHARGE) {
            var numSymbols = DISCHARGE - value.length();
            return COMPLEMENT_SYMBOL.repeat(numSymbols);
        }

        return "";
    }
}
