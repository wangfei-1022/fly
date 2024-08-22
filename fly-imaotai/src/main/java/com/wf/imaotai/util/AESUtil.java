package com.wf.imaotai.util;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AESUtil {

    private final static String SALT = "2af72f100c356273d46284f6fd1dfc08";
    private final static String AES_KEY = "qbhajinldepmucsonaaaccgypwuvcjaa";
    private final static String AES_IV = "2018534749963515";

    public static String signature(String content, long time) {

        String text = SALT + content + time;
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            md5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }


    public static String AesEncrypt(String params) {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, AES_KEY.getBytes(), AES_IV.getBytes());
        try {
            aes.encryptBase64(params);
        }catch (Exception exception) {
            System.out.println(exception);
        }
        return aes.encryptBase64(params);
    }

    public static void main(String[] args) {
        String originalString = "123456";
        String encryptedString = AesEncrypt(originalString);
        System.out.println("Original: " + originalString);
        System.out.println("Encrypted: " + encryptedString);
    }
}
