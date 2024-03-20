package org.example;

import org.example.interfaces.HashFunction;
import org.example.laboratory7.DigitalSignature;
import org.example.laboratory7.SHA256;
import org.example.laboratory7.XORHashFunction;

public class App {
    public static void main(String[] args) {
        hash(new XORHashFunction(), "Використання XOR хеш функції");
        hash(new SHA256(), "Використання SHA256 хеш функції");
        digitalSign();
    }

    private static void hash(HashFunction hashFunction, String message) {
        String value1 = "Hello World";
        String value2 = "Hello world";

        String hash1 = hashFunction.encoder(value1);
        String hash2 = hashFunction.encoder(value2);

        System.out.println(message);
        System.out.printf("Хеш код для речення \"%s\" становить: %s%n", value1, hash1);
        System.out.printf("Хеш код для речення \"%s\" становить: %s%n", value2, hash2);
        System.out.printf("%s == %s - %b%n%n", value1, value2, hash1.equals(hash2));
    }

    private static void digitalSign() {
        DigitalSignature sign = new DigitalSignature(65537);
        String value1 = "Hello World";
        String value2 = "Hello world";
        String signValue = sign.create(value1);
        boolean compliance1 = sign.isCompliance(value1, signValue);
        boolean compliance2 = sign.isCompliance(value2, signValue);

        System.out.println("Електронний цифровий підпис");
        System.out.printf("ЕЦП: %s%n", signValue);
        System.out.printf("Вхідне значення: %s%n", value1);
        System.out.printf("Підпис справжній: %b%n", compliance1);
        System.out.printf("Змінене повідомлення: %s%n", value2);
        System.out.printf("Підпис справжній: %b%n", compliance2);
    }
}
