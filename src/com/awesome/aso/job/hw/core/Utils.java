package com.awesome.aso.job.hw.core;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

public class Utils {

    public static byte[] toUnicodeByteArr(String str) {
        char[] chars = str.toCharArray();
        int length = chars.length / 2;
        byte[] byteArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int digit = (Character.digit(chars[i << 1], 16) << 4) | Character.digit(chars[(i << 1) + 1], 16);
            if (digit > 127) {
                digit -= 256;
            }
            byteArr[i] = (byte) digit;
        }
        return byteArr;
    }

    public static String crc32Encoding(String signature) {
        signature = BaseStringUtils.encoding(toUnicodeByteArr(signature));
        CRC32 crc32 = new CRC32();
        try {
            crc32.update(signature.getBytes("UTF-8"));
            return Long.toHexString(crc32.getValue());
        } catch (UnsupportedEncodingException e) {
            // b.e(UpgradeRequest.TAG, "UnsupportedEncoding!");
        }
        return null;
    }
}
