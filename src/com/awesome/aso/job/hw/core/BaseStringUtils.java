package com.awesome.aso.job.hw.core;

public class BaseStringUtils {
    private static char[] a = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '=' };
    private static byte[] b = new byte[] { (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 62, (byte) -1, (byte) -1, (byte) -1, (byte) 63, (byte) 52,
            (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 58, (byte) 59, (byte) 60, (byte) 61, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10,
            (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23,
            (byte) 24, (byte) 25, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30,
            (byte) 31, (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41, (byte) 42, (byte) 43,
            (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1,
            (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1 };

    public static final int MASK_8BIT = 255;

    public static String encoding(byte[] bArr) {
        return encoding(bArr, bArr.length);
    }

    public static String encoding(byte[] bArr, int i) {
        char[] cArr = new char[(((i + 2) / 3) << 2)];
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3 += 3) {
            boolean obj;
            boolean obj2;
            int i4 = (bArr[i3] & MASK_8BIT) << 8;
            if (i3 + 1 < i) {
                i4 |= bArr[i3 + 1] & MASK_8BIT;
                obj = true;
            } else {
                obj = false;
            }
            i4 <<= 8;
            if (i3 + 2 < i) {
                i4 |= bArr[i3 + 2] & MASK_8BIT;
                obj2 = true;
            } else {
                obj2 = false;
            }
            cArr[i2 + 3] = a[obj2 ? i4 & 63 : 64];
            int i5 = i4 >> 6;
            cArr[i2 + 2] = a[obj ? i5 & 63 : 64];
            i4 = i5 >> 6;
            cArr[i2 + 1] = a[i4 & 63];
            cArr[i2] = a[(i4 >> 6) & 63];
            i2 += 4;
        }
        return new String(cArr);
    }

    public static byte[] decoding(String str) {
        int i;
        int i2 = 0;
        int length = str.length();
        for (i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt > '\u00ff' || b[charAt] < (byte) 0) {
                length--;
            }
        }
        i = (length / 4) * 3;
        if (length % 4 == 3) {
            i += 2;
        }
        if (length % 4 == 2) {
            i++;
        }
        byte[] bArr = new byte[i];
        i = 0;
        length = 0;
        int i3 = 0;
        while (i2 < str.length()) {
            char charAt2 = str.charAt(i2);
            int i4 = charAt2 > '\u00ff' ? -1 : b[charAt2];
            if (i4 >= 0) {
                int i5 = length << 6;
                length = i3 + 6;
                i3 = i5 | i4;
                if (length >= 8) {
                    i4 = length - 8;
                    length = i + 1;
                    bArr[i] = (byte) (i3 >> i4);
                    i = length;
                    length = i3;
                    i3 = i4;
                } else {
                    int i6 = i3;
                    i3 = length;
                    length = i6;
                }
            }
            i2++;
        }
        return i != bArr.length ? null : bArr;
    }

}
