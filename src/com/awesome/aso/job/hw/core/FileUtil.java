package com.awesome.aso.job.hw.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

    // oldMD5Code
    public static String getFileMD5(String file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest instance = MessageDigest.getInstance("md5");
            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = fis.read(buff)) != -1) {
                instance.update(buff, 0, len);
            }
            return byteArrToHex(instance.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //
    public static String getHashCode(File file) {
        byte[] bytes = getBytes(file);
        if (bytes == null) {
            return "No_hash";
        }
        try {
            byte[] lenByteArr = Long.toString(file.length()).getBytes("UTF-8");
            byte[] hashByteArr = new byte[(bytes.length + lenByteArr.length)];
            System.arraycopy(bytes, 0, hashByteArr, 0, bytes.length);
            System.arraycopy(lenByteArr, 0, hashByteArr, bytes.length, lenByteArr.length);
            // String hashStr = "";
            MessageDigest instance = MessageDigest.getInstance("md5");
            instance.update(hashByteArr);
            return byteArrToHex(instance.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getBytes(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream(32);
            byte[] buff = new byte[32];
            int read = fis.read(buff);
            if (read != -1) {
                baos.write(buff, 0, read);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String byteArrToHex(byte[] bArr) {
        int i = 0;
        char[] hax = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] cArr2 = new char[(bArr.length << 1)];
        int length = bArr.length;
        int i2 = 0;
        while (i < length) {
            byte b = bArr[i];
            int i3 = i2 + 1;
            cArr2[i2] = hax[(b >>> 4) & 15];
            i2 = i3 + 1;
            cArr2[i3] = hax[b & 15];
            i++;
        }
        return new String(cArr2);
    }

}
