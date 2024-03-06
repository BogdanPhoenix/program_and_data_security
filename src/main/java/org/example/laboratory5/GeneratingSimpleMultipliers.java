package org.example.laboratory5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratingSimpleMultipliers {
    private static final int FIRST_PRIME = 2;

    private int number;
    private final List<Integer> primeNums = new ArrayList<>();
    private List<Boolean> primeNumsBool = new ArrayList<>();

    public GeneratingSimpleMultipliers() {
        number = FIRST_PRIME;
    }

    public void createSeriesPrimeNums(int number) {
        if(this.number == number) {
            return;
        }
        else if(this.number > number) {
            truncateArray(number);
        }
        else {
            expandArray(number);
        }
        this.number = number;
        primes();
    }

    private void truncateArray(int newSize) {
        if (primeNumsBool.size() > newSize + 1) {
            primeNumsBool.subList(newSize + 1, primeNumsBool.size()).clear();
        }
    }

    private void expandArray(int size) {
        primeNumsBool = new ArrayList<>(Collections.nCopies(size, true));
        sieveOfEratosthenes();
    }

    private void sieveOfEratosthenes() {
        for(int i = FIRST_PRIME; i * i < primeNumsBool.size(); ++i) {
            if(Boolean.TRUE.equals(primeNumsBool.get(i))) {
                searchingNonPrimeNumbers(i);
            }
        }
    }

    private void searchingNonPrimeNumbers(int index) {
        for(int j = index * index; j < primeNumsBool.size(); j += index) {
            primeNumsBool.set(j, false);
        }
    }

    private void primes() {
        primeNums.clear();
        for(int i = FIRST_PRIME; i < primeNumsBool.size(); ++i) {
            if(Boolean.TRUE.equals(primeNumsBool.get(i))) {
                primeNums.add(i);
            }
        }
    }

    public List<Integer> getPrimeNums(){
        return primeNums;
    }

    public int getLastPrime() {
        return primeNums.getLast();
    }

    public int getFirstPrime() {
        return FIRST_PRIME;
    }
}
