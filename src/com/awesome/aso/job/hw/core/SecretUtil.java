package com.awesome.aso.job.hw.core;

import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SecretUtil {
    public static String hmacSha256Base64(String str, String str2) {
        try {
            byte[] b = hmacSha1(str.getBytes("utf-8"), str2.getBytes("utf-8"));
            if (b != null && b.length > 0) {
                return new String(Base64.encode(b, 0), "utf-8");
            }
        } catch (Throwable e) {
            System.out.println("SecretUtil" + "hmacSha256Base64 error");
        }
        return null;
    }

    public static String hmacSHA256HexStr(byte[] bArr, byte[] bArr2) {
        String str = null;
        byte[] b = HmacSHA256(bArr, bArr2);
        if (b != null && b.length > 0) {
            str = "";
            int i = 0;
            while (i < b.length) {
                String toHexString = Integer.toHexString(b[i] & 255);
                if (toHexString.length() == 1) {
                    str = str + "0";
                }
                i++;
                str = str + toHexString;
            }
        }
        return str;
    }

    public static String hmacSha1Base64(String str, String str2) {
        try {
            byte[] c = hmacSha1(str.getBytes("utf-8"), str2.getBytes("utf-8"));
            if (c != null && c.length > 0) {
                return new String(Base64.encode(c, 0), "utf-8");
            }
        } catch (Throwable e) {
            // b.a("SecretUtil", "hmacSha1 error", e);
        }
        return null;
    }

    private static byte[] HmacSHA256(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return new byte[0];
        }
        try {
            Key secretKeySpec = new SecretKeySpec(bArr2, "HmacSHA256");
            Mac instance = Mac.getInstance("HmacSHA256");
            instance.init(secretKeySpec);
            return instance.doFinal(bArr);
        } catch (Throwable e) {
            // b.a("SecretUtil", "HmacSHA256 error", e);
            return new byte[0];
        }
    }

    private static byte[] hmacSha1(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return new byte[0];
        }
        try {
            Key secretKeySpec = new SecretKeySpec(bArr2, "HmacSHA1");
            Mac instance = Mac.getInstance("HmacSHA1");
            instance.init(secretKeySpec);
            return instance.doFinal(bArr);
        } catch (Throwable e) {
            // b.a("SecretUtil", "hmacSha1 error", e);
            return new byte[0];
        }
    }

}
