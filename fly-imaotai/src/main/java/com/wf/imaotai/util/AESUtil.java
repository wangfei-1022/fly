package com.wf.imaotai.util;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
        return aes.encryptBase64(params);
    }

    public static String AesDecrypt(String params) {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, AES_KEY.getBytes(), AES_IV.getBytes());
        return aes.decryptStr(params);
    }

    public static String decodeBase64String() {
        String cookie = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTcyNjY0MzEyMSwidXNlcklkIjoxMTE2OTc2NzA0LCJkZXZpY2VJZCI6ImU5NjAxOGVlLTM5ZmEtNDY2NS05MTdjLWUyYWJlNjhkNGIzNyIsImlhdCI6MTcyNDA1MTEyMX0.V4taJIRjQQWkRM0-OP-TamNLrH4GaMya2P0frM2NTYA";
        String[] cookieString = cookie.split("\\.");
        byte[] decodedCookieBytes = Base64.getDecoder().decode(cookieString[1]);
        System.out.println(new String(decodedCookieBytes));

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTcyNjY0MzEyMSwidXNlcklkIjoxMTE2OTc2NzA0LCJkZXZpY2VJZCI6ImU5NjAxOGVlLTM5ZmEtNDY2NS05MTdjLWUyYWJlNjhkNGIzNyIsImlhdCI6MTcyNDA1MTEyMX0.MDUdgI4qbawWfob4I3_BXtivVAv_80DG2dyEAiwWjz8";
        String[] s = token.split("\\.");
        byte[] decodedBytes = Base64.getDecoder().decode(s[1]);
        System.out.println(new String(decodedBytes));
        return token;
    }
}
