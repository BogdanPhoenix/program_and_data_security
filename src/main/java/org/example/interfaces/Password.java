package org.example.interfaces;

public interface Password {
    String encoder(String password);
    String decoder(String encryptedPassword);
}
