package org.example;

import org.example.interfaces.Password;
import org.example.laboratory3.SimpleRearrangement;
import org.example.laboratory3.SkitalCipher;

public class App {
    private static final String[] INPUT_DATA = {
            "Wild winds howl loud", "Quick fox jumps high"
    };

    public static void main(String[] args) {
        System.out.println("Шифр простої перестановки");
        cipher(new SimpleRearrangement());

        System.out.println("Шифр Скитала");
        cipher(new SkitalCipher());
    }

    private static void cipher(Password password) {
        for(var text : INPUT_DATA) {
            var encoder = password.encoder(text);
            var decoder = password.decoder(encoder);

            System.out.println("\tВведені дані: " + text);
            System.out.println("\tЗашифровані дані: " + encoder);
            System.out.println("\tРозшифровані дані: " + decoder);
            System.out.println();
        }

        System.out.println();
    }
}
