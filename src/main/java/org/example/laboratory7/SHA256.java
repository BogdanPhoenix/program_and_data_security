package org.example.laboratory7;

import org.example.interfaces.HashFunction;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SHA256 implements HashFunction {
    private static final int BLOCK_SIZE = 512;
    private static final int NUMBER_BLOCKS = 64;
    private static final int WORD_SIZE = 32;

    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5db11, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    private static class Context {
        byte[] data = new byte[NUMBER_BLOCKS];
        int dataLen = 0;
        long bitLen = 0;
        int[] state = {
                0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
                0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
        };
    }

    private static int rotateRight(int a, int b) {
        return (a >>> b) | (a << (32 - b));
    }

    private static int bitwiseXorOfAnds(int x, int y, int z) {
        return (x & y) ^ (~x & z);
    }

    private static int bitwiseMajority(int x, int y, int z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    private static int rotatedXor(int x) {
        return rotateRight(x, 2) ^ rotateRight(x, 13) ^ rotateRight(x, 22);
    }

    private static int bitwiseXorRotateRight(int x) {
        return rotateRight(x, 6) ^ rotateRight(x, 11) ^ rotateRight(x, 25);
    }

    private static int sigma0(int x) {
        return rotateRight(x, 7) ^ rotateRight(x, 18) ^ (x >>> 3);
    }

    private static int sigma1(int x) {
        return rotateRight(x, 17) ^ rotateRight(x, 19) ^ (x >>> 10);
    }

    private Context ctx;

    @Override
    public String encoder(String value) {
        byte[] message = value.getBytes(StandardCharsets.UTF_8);
        ctx = new Context();

        updateData(message);
        byte[] hash = processFinalBlock();

        return bytesToHexString(hash);
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    private void updateData(byte[] data) {
        for (byte b : data) {
            ctx.data[ctx.dataLen] = b;
            ++ctx.dataLen;

            if (ctx.dataLen != NUMBER_BLOCKS) {
                continue;
            }

            compress(ctx.data);
            ctx.bitLen += BLOCK_SIZE;
            ctx.dataLen = 0;
        }
    }

    private void compress(byte[] data) {
        int[] m = transformByteArray(data);

        int a = ctx.state[0];
        int b = ctx.state[1];
        int c = ctx.state[2];
        int d = ctx.state[3];
        int e = ctx.state[4];
        int f = ctx.state[5];
        int g = ctx.state[6];
        int h = ctx.state[7];

        for (int i = 0; i < 64; ++i) {
            int t1 = h + bitwiseXorRotateRight(e) + bitwiseXorOfAnds(e,f,g) +K[i] + m[i];
            int t2 = rotatedXor(a) + bitwiseMajority(a,b,c);
            h = g;
            g = f;
            f = e;
            e = d + t1;
            d = c;
            c = b;
            b = a;
            a = t1 + t2;
        }

        ctx.state[0] += a;
        ctx.state[1] += b;
        ctx.state[2] += c;
        ctx.state[3] += d;
        ctx.state[4] += e;
        ctx.state[5] += f;
        ctx.state[6] += g;
        ctx.state[7] += h;
    }

    private int[] transformByteArray(byte[] data) {
        int[] arr = new int[NUMBER_BLOCKS];
        int i = 0;

        for (int j = 0; i < 16; ++i, j += 4) {
            arr[i] = (data[j] & 0xff) << 24 | (data[j + 1] & 0xff) << 16 | (data[j + 2] & 0xff) << 8 | (data[j + 3] & 0xff);
        }
        for ( ; i < 64; ++i) {
            arr[i] = sigma1(arr[i - 2]) + arr[i - 7] + sigma0(arr[i - 15]) + arr[i - 16];
        }

        return arr;
    }

    private byte[] processFinalBlock() {
        int i = ctx.dataLen;
        byte[] hash = new byte[WORD_SIZE];

        if (ctx.dataLen < 56) {
            padData(i, 56);
        }
        else {
            processData(i);
        }

        ctx.bitLen += ctx.dataLen * 8L;
        ctx.data[63] = (byte) (ctx.bitLen);
        ctx.data[62] = (byte) (ctx.bitLen >>> 8);
        ctx.data[61] = (byte) (ctx.bitLen >>> 16);
        ctx.data[60] = (byte) (ctx.bitLen >>> 24);
        ctx.data[59] = (byte) (ctx.bitLen >>> 32);
        ctx.data[58] = (byte) (ctx.bitLen >>> 40);
        ctx.data[57] = (byte) (ctx.bitLen >>> 48);
        ctx.data[56] = (byte) (ctx.bitLen >>> 56);
        compress(ctx.data);

        for (i = 0; i < 4; ++i) {
            shiftAndStore(hash, i);
        }

        return hash;
    }

    private void processData(int i) {
        padData(i, NUMBER_BLOCKS);
        compress(ctx.data);
        Arrays.fill(ctx.data, (byte) 0x00);
    }

    private void padData(int i, int x) {
        ctx.data[i++] = (byte) 0x80;
        while (i < x) {
            ctx.data[i++] = 0x00;
        }
    }

    private void shiftAndStore(byte[] hash, int i) {
        for(int j = 0; j < ctx.state.length; ++j) {
            hash[i + (4 * j)] = (byte) ((ctx.state[j] >>> (24 - i * 8)) & 0x000000ff);
        }
    }
}
