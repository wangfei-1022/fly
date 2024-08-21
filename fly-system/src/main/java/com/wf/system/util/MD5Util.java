package com.wf.system.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    /**
     * 使用MD5算法对字符串进行加密。
     *
     * @param input 待加密的字符串
     * @return 加密后的MD5散列值字符串
     */
    public static String encryptToMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(input.getBytes());

            // 将字节数组转换成十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public static void main(String[] args) {
        String originalString = "123456";
        String encryptedString = encryptToMD5(originalString);
        System.out.println("Original: " + originalString);
        System.out.println("Encrypted: " + encryptedString);
    }

}
