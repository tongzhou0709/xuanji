package com.xuanji.utils;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.nio.charset.StandardCharsets;

public class SM4Utils {

    private static final String DEFAULT_KEY = "xuanji1234567890";

    public static String encrypt(String plainText) {
        return encrypt(plainText, DEFAULT_KEY);
    }

    public static String encrypt(String plainText, String key) {
        try {
            SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes(StandardCharsets.UTF_8));
            return sm4.encryptHex(plainText);
        } catch (Exception e) {
            throw new RuntimeException("SM4加密失败", e);
        }
    }

    public static String decrypt(String cipherText) {
        return decrypt(cipherText, DEFAULT_KEY);
    }

    public static String decrypt(String cipherText, String key) {
        try {
            SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes(StandardCharsets.UTF_8));
            return sm4.decryptStr(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("SM4解密失败", e);
        }
    }

    public static boolean matches(String plainText, String cipherText) {
        try {
            String decrypted = decrypt(cipherText);
            return plainText.equals(decrypted);
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String password = "123456Qq!";
        String encrypted = encrypt(password);
        String decrypted = decrypt(encrypted);

        System.out.println("原始密码: " + password);
        System.out.println("SM4加密: " + encrypted);
        System.out.println("SM4解密: " + decrypted);
        System.out.println("匹配结果: " + matches(password, encrypted));
    }
}