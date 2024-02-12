package org.example;

import org.example.implementation.CaesarsCipher;
import org.example.implementation.PlayfairCipher;
import org.example.implementation.PolybiusSquare;
import org.example.implementation.VigenereCipher;
import org.example.interfaces.Password;

public class App {
    private static final String[] INPUT_DATA = {
            "Wild winds howl loud", "Test", "Quick fox jumps high"
    };

    public static void main(String[] args) {
        System.out.println("Шифр Цезаря");
        cipher(new CaesarsCipher());

        System.out.println("Шифр Плейфера");
        cipher(new PlayfairCipher());

        System.out.println("Квадрат Полібія");
        cipher(new PolybiusSquare());

        System.out.println("Шифр Віженера");
        cipher(new VigenereCipher());
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
