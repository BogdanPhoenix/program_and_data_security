package org.example;

import org.example.laboratory5.DecompositionPrimeFactors;
import org.example.laboratory5.RSA;

public class App {
    private static final String[] INPUT_DATA = {
            "Wild winds howl loud", "Quick fox jumps high"
    };

    public static void main(String[] args) {
        rsa();
        decomposition();
    }

    private static void rsa() {
        System.out.println("Використання RSA");
        cipher(new RSA(59, 113), 401);
    }

    private static void cipher(RSA password, int openKey) {
        for(var text : INPUT_DATA) {
            var encoder = password.encoder(text, openKey);
            var decoder = password.decoder(encoder, openKey);

            System.out.println("\tВведені дані: " + text);
            System.out.println("\tЗашифровані дані: " + encoder);
            System.out.println("\tРозшифровані дані: " + decoder);
            System.out.println();
        }

        System.out.println();
    }

    private static void decomposition() {
        primeNums(4358615L);
        primeNums(Integer.MAX_VALUE);
        primeNums(Integer.MAX_VALUE - 1);
        primeNums(Long.MAX_VALUE);
        primeNums(Long.MAX_VALUE - 1);
    }

    private static void primeNums(long num) {
        var result = DecompositionPrimeFactors.numberPrimeFactors(num);
        System.out.println("Число: " + num + " - прості множники: " + result);
    }
}
