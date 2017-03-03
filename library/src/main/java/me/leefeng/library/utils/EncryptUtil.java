package me.leefeng.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by limxing on 15/12/1.
 */
public class EncryptUtil {
    /**
     *
     * @param origin
     */
    public static String MD5Encode(String origin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(md.digest(origin.getBytes()));
        } catch (Exception ex) {
        }
        return null;
    }
    /**
     *
     * @param b
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    /**
     *
     * @param b
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
 /**
     *
     * @param e
     * @return
     * @throws Exception
     */
    public static String fileToMD5(File e) throws Exception {
        String md5 = null;
        MessageDigest md = null;
        FileInputStream in = null;
        String var9;
        try {
            if (e.exists() && e.isFile()) {
                byte[] buffer = new byte[1024];
                boolean len = false;
                md = MessageDigest.getInstance("MD5");
                in = new FileInputStream(e);
                int len1;
                while ((len1 = in.read(buffer)) != -1) {
                    md.update(buffer, 0, len1);
                }
                BigInteger bigInt = new BigInteger(1, md.digest());
                md5 = bigInt.toString(16);
            }
            var9 = md5;
        } catch (Exception var16) {
            var16.printStackTrace();
            throw var16;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }
        }
        return var9;
    }
}

