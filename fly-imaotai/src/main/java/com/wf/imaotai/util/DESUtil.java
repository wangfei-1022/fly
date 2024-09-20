package com.wf.imaotai.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

public class DESUtil {

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";

    /**
     * 加密/解密算法-工作模式-填充模式
     * DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";

    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 生成key
     *
     * @param password 密钥字符串
     * @return 密钥对象
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }

    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data     待加密字符串
     * @return 加密后内容(十六进制字符串)
     */
    public static String encrypt(String password, String data) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));
            //return new String(Base64.getEncoder().encode(bytes));
            return byte2HexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data     待解密字符串(十六进制字符串)
     * @return 解密后内容
     */
    public static String decrypt(String password, String data) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] bytes = hexStringToByteArray(data);
            return new String(cipher.doFinal(bytes), CHARSET);
            //return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * byte数组转十六进制
     *
     * @param bytes
     * @return
     */
    public static String byte2HexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        if (bytes != null) {
            for (Byte b : bytes) {
                hex.append(String.format("%02X", b.intValue() & 0xFF));
            }
        }
        return hex.toString();
    }

    /**
     * 十六进制转byte数组
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        try {
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i + 1), 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    //测试
    public static void main(String[] args) throws Exception {
        String source = "dbdd75c34fc530d971786fd1c11c594179ab75eb787aac822f4a5a34b24ecd25138e8dbdee4ba0c5914df475b5e44bdb50ca49320eefcb70c614c4efc6aac096449cb3b1776cf8912a18f68ee1dfa1c31a5e8bc41df1b476f640c11b1cf9bf58ce69ac5617d4855f45d42c98dde9a7ae";
//        System.out.println("原  文: " + source);
        String password = "f2a0ab415ab7b72142325aeacb35c5da454dc278648bef4212d0b47be4aeea4b";
//        String encryptData = encrypt(password, source);
//        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt(password, source);
        System.out.println("解密后: " + decryptData);
    }

    static double a[] = {1,2,3};
    static double b[] = {9,9,7};

    static void test() {

    }

    static float sqrtSum(double[] x) {
        double y = 0.0;
        for (int i = 0; i < x.length; i++) {
            y += Math.sqrt(x[i]);
        }
        return (float)y;
    }

}
