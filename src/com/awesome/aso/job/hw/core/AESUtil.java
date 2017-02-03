package com.awesome.aso.job.hw.core;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.IllegalFormatException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    public static String sha256HexStr(String str) {
        try {
            byte[] bytes = str.getBytes("utf-8");
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(bytes);
            byte[] digest = instance.digest();
            String str2 = "";
            int i = 0;
            while (i < digest.length) {
                String toHexString = Integer.toHexString(digest[i] & 255);
                if (toHexString.length() == 1) {
                    str2 = str2 + "0";
                }
                i++;
                str2 = str2 + toHexString;
            }
            return str2;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e2) {
            return null;
        } catch (IllegalArgumentException e3) {
            return null;
        } catch (Exception e4) {
            return null;
        }
    }

    public static byte[] copyArrByLength(byte[] orgArr, int length) {
        byte[] obj = new byte[length];
        System.arraycopy(orgArr, 0, obj, 0, Math.min(orgArr == null ? 0 : orgArr.length, length));
        return obj;
    }

    public static Cipher getCipher(int opmode, byte[] key, byte[] iv) throws NoSuchAlgorithmException, InvalidKeyException, GeneralSecurityException {
        Key secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(opmode, secretKeySpec, new IvParameterSpec(iv));
        return instance;
    }

    public static String aesCipherBase64(String data, byte[] secretKey, byte[] iv) throws Exception {
        if (secretKey.length > 16) {
            secretKey = copyArrByLength(secretKey, 16);
        }
        byte[] subidArr = getCipher(1, secretKey, iv).doFinal(data.getBytes("UTF-8"));
        return BaseStringUtils.encoding(subidArr).replaceAll("\n", "").replaceAll("\r", "");

    }

    public static String encrypASCIIKey(String str, String secretKey, byte[] iv) throws InvalidKeyException, NoSuchAlgorithmException,
            UnsupportedEncodingException, GeneralSecurityException {
        if (secretKey == null) {
            return null;
        }
        if (secretKey.length() < 16) {
            return null;
        }
        if (secretKey.length() > 16) {
            secretKey = secretKey.substring(0, 16);
        }
        byte[] enncData = getCipher(1, secretKey.getBytes("ASCII"), iv).doFinal(str.getBytes("UTF-8"));
        String str3 = "";
        for (int i = 0; i < enncData.length; i++) {
            byte b = enncData[i];
            String toHexString = Integer.toHexString(b & 255);
            str3 = toHexString.length() == 1 ? str3 + "0" + toHexString : str3 + toHexString;
        }
        return str3.toUpperCase(Locale.US);
    }

    public static String sha256Digest(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(str.getBytes("utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            byte[] digestArr = instance.digest();
            for (int i = 0; i < digestArr.length; i++) {
                stringBuffer.append(String.format("%02X", new Byte[] { Byte.valueOf(digestArr[i]) }));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e2) {
            return null;
        } catch (IllegalFormatException e3) {
            return null;
        } catch (Exception e4) {
            return null;
        }
    }

}
