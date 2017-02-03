package com.awesome.aso.job.hw.core;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class EncryptKeyUtils {

    public static String publicKeyEncryptData(byte[] data, String encodedKey) throws Exception {
        byte[] keyByte = charToByte(encodedKey.toCharArray());
        X509EncodedKeySpec key = new X509EncodedKeySpec(keyByte);
        Key publicKey = KeyFactory.getInstance("RSA").generatePublic(key);
        return BaseStringUtils.encoding(encryptByPublicKey(publicKey, data));
    }

    private static byte[] encryptByPublicKey(Key key, byte[] bArr) throws Exception {
        // TODO

        // Security.addProvider(new
        // org.bouncycastle.jce.provider.BouncyCastleProvider());

        byte[] doFinal;
        Cipher instance = Cipher.getInstance("RSA", "BC");
        instance.init(1, key);
        int length = bArr.length;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        int i2 = 0;
        while (length - i2 > 0) {
            doFinal = length - i2 > 117 ? instance.doFinal(bArr, i2, 117) : instance.doFinal(bArr, i2, length - i2);
            byteArrayOutputStream.write(doFinal, 0, doFinal.length);
            i2 = i + 1;
            int i3 = i2;
            i2 *= 117;
            i = i3;
        }
        doFinal = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return doFinal;
    }

    public static byte[] charToByte(char[] cArr) throws IllegalArgumentException {
        int i = 0;
        int length = cArr.length;
        if ((length & 1) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }
        byte[] bArr = new byte[(length >> 1)];
        int i2 = 0;
        while (i < length) {
            char char1 = cArr[i];

            int int1 = charTOHaxDigit(char1, i);

            int1 <<= 4;

            i++;

            char char2 = cArr[i];

            int int2 = charTOHaxDigit(char2, i);

            bArr[i2] = (byte) (int1 | int2);
            i++;
            // bArr[i2] = (byte) (( << 4) | charTOHaxDigit(cArr[i],i));

            i2++;
        }
        return bArr;
    }

    private static int charTOHaxDigit(char c, int i) throws IllegalArgumentException {
        int digit = Character.digit(c, 16);
        if (digit != -1) {
            return digit;
        }
        throw new IllegalArgumentException("Illegal hexadecimal character " + c + " at index " + i);
    }
}
