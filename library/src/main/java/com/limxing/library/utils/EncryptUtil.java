package com.limxing.library.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by limxing on 15/12/1.
 */
public class EncryptUtil {
    /**
     * MD5加密.
     *
     * @param origin
     *            原始加密字符串
     * @return 加密后字符串
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
     * 转换字节数组为16进制字串.
     *
     * @param b
     *            字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    /**
     * 单字节转十六进制字符串.
     *
     * @param b
     *            单字节数字
     * @return 十六进制字符串
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

}

///**
// * 加密 算法 MD5，算法Base64加密
// *
// */
//{
//
//    /**
//     * BASE64加密.
//     *
//     * @param str
//     *            加密字符串
//     * @return 加密后字符串
//     */
//    @SuppressWarnings("unused")
//    private static String BASE64Encode(String str) {
//        return new String(Base64.decode(str.toCharArray()));
//    }
//
//
//
//
//
//
//    public static String getMD5_Base64(String input) {
//        MessageDigest digest = null;
//        try {
//            digest = MessageDigest.getInstance("MD5");
//            digest.update(input.getBytes("UTF-8"));
//        } catch (java.io.UnsupportedEncodingException ex) {
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//        byte[] rawData = digest.digest();
//        char[] encoded = Base64.encode(rawData);
//        return new String(encoded);
//    }
//
//    public static void main(String[] args) {
//        System.out.println(MD5Encode("123456"));
//    }
//}
