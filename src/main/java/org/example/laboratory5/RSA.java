package org.example.laboratory5;

public class RSA {
    private final int p;
    private final int q;

    public RSA(int p, int q) {
        this.p = p;
        this.q = q;
    }

    public String encoder(String password, int openKey) {
        validOpenKey(openKey);

        var mod = module();
        var chars = password.chars()
                .boxed()
                .toArray(Integer[]::new);

        return work(chars, openKey, mod);
    }

    public String decoder(String encryptedPassword, int openKey) {
        validOpenKey(openKey);

        var d = closeKey(openKey);
        var mod = module();
        var encryptedBytes = encryptedPassword.chars()
                .boxed()
                .toArray(Integer[]::new);

        return work(encryptedBytes, d, mod);
    }

    private void validOpenKey(int openKey) throws RuntimeException {
        if(isCorrectOpenKey(openKey)) {
            return;
        }

        throw new RuntimeException("""
                The public key you entered does not meet the following criteria:
                - the number must be prime;
                - the number must be less than f;
                - the number must be mutually prime with f.
                """);
    }

    private boolean isCorrectOpenKey(int openKey) {
        var f = functionEuler();
        var result = extendedEuclid(openKey, f);
        return result.gcd() == 1;
    }

    protected int closeKey(int openKey) {
        var f = functionEuler();
        return modInverse(openKey, f);
    }

    private int modInverse(int a, int m) {
        a = Math.abs(a);
        m = Math.abs(m);

        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }

        return -1;
    }

    private String work(Integer[] value, int key, int help) {
        var result = new StringBuilder();

        for (var c : value) {
            var m = RSA.findRemainder(c, key, help);
            result.append((char) m);
        }

        return result.toString();
    }

    private int functionEuler() {
        return (p - 1) * (q - 1);
    }

    private int module() {
        return p * q;
    }

    static int findRemainder(int a, int n, int k) {
        int r = a % k;
        int base = 1;
        int exponent = n;

        while (exponent > 1) {
            base = (base * r) % k;
            --exponent;
        }

        return (r * base) % k;
    }

    private EuclidData extendedEuclid(int a, int b) {
        if (a == 0) {
            return new EuclidData(b, 0, 1);
        }

        var result = extendedEuclid(b % a, a);

        var newX = result.y() - (b / a) * result.x();
        var newY = result.x();

        return new EuclidData(result.gcd(), newX, newY);
    }
}
