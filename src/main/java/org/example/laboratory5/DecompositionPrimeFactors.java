package org.example.laboratory5;

import java.util.ArrayList;
import java.util.List;

public class DecompositionPrimeFactors {
    private DecompositionPrimeFactors() {
    }

    public static List<Long> numberPrimeFactors(long number) {
        long p = 2L;
        List<Long> primeNumbers = new ArrayList<>();

        do {
            if(number % p == 0) {
                primeNumbers.add(p);
                number /= p;
            }
            else {
                ++p;
            }
        }while(number >= p * p);
        primeNumbers.add(number);

        return primeNumbers;
    }
}