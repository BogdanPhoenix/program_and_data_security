package org.example.laboratory7;

import org.example.laboratory5.RSA;

public class DigitalSignature {
    private final int openKey;
    private final RSA rsa;
    private final SHA256 sha256;

    public DigitalSignature(int openKey) {
        this.openKey = openKey;
        this.rsa = new RSA(157, 199);
        this.sha256 = new SHA256();
    }

    public String create(String value) {
        var hash = sha256.encoder(value);
        return rsa.encoder(hash, openKey);
    }

    public boolean isCompliance(String value, String sign) {
        var hash = sha256.encoder(value);
        var decoder = rsa.decoder(sign, openKey);
        return hash.equals(decoder);
    }
}
