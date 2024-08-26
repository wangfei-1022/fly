package com.wf.imaotai.util;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import okhttp3.*;

import java.io.IOException;
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
        try {
            aes.encryptBase64(params);
        }catch (Exception exception) {
            System.out.println(exception);
        }
        return aes.encryptBase64(params);
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

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"itemInfoList\":[{\"itemId\":\"10941\",\"count\":1}],\r\n    \"actParam\":\"IdiwwdtRdEBhdeHkaJbq1KaW+LwQpVuZhH2uIaRKC41kqQyRplBiH4D9NXHDRD0y1D3syYKc3xmQzsNpLGCJGD4RX6OPmIW4TPXFadeIWIrT/kp1ZV/3UqvTUXIV5gPKm/a7cqI6gLTuYxlGJ8K+IRZUwDwj1wbHxUzGdN+PWks=\",\r\n    \"sessionId\":\"1163\",\r\n    \"shopId\":\"231310108005\",\r\n    \"userId\":\"1116976704\"\r\n}");
        Request request = new Request.Builder()
                .url("https://app.moutai519.com.cn/xhr/front/mall/reservation/add")
                .method("POST", body)
                .addHeader("MT-Lat", "31.15976")
                .addHeader("MT-Lng", "121.463072")
                .addHeader("MT-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTcyNjY0MzEyMSwidXNlcklkIjoxMTE2OTc2NzA0LCJkZXZpY2VJZCI6ImU5NjAxOGVlLTM5ZmEtNDY2NS05MTdjLWUyYWJlNjhkNGIzNyIsImlhdCI6MTcyNDA1MTEyMX0.MDUdgI4qbawWfob4I3_BXtivVAv_80DG2dyEAiwWjz8")
                .addHeader("MT-Info", "028e7f96f6369cafe1d105579c5b9377")
                .addHeader("MT-Device-ID", "e96018ee-39fa-4665-917c-e2abe68d4b37")
                .addHeader("MT-APP-Version", "1.7.1")
                .addHeader("User-Agent", "iOS;16.3;Apple;?unrecognized?")
                .addHeader("Content-Type", "application/json")
                .addHeader("userId", "1116976704")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(new String(response.body().bytes(), "UTF-8"));
    }
}
