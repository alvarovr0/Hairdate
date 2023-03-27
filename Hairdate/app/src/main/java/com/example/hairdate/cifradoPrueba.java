package com.example.hairdate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import java.security.SecureRandom;

public class cifradoPrueba {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    private static final String SECRET_KEY = "YOUR_SECRET_KEY";
    private static final int IV_LENGTH = 16;

    public static String encrypt(String password) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET), "AES");
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedPassword = cipher.doFinal(password.getBytes(CHARSET));
        byte[] combined = new byte[iv.length + encryptedPassword.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedPassword, 0, combined, iv.length, encryptedPassword.length);
        return Base64.encodeToString(combined, Base64.NO_WRAP);
    }

    public static String decrypt(String encryptedPassword) throws Exception {
        byte[] combined = Base64.decode(encryptedPassword, Base64.NO_WRAP);
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        byte[] encryptedBytes = new byte[combined.length - iv.length];
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedPassword = cipher.doFinal(encryptedBytes);
        return new String(decryptedPassword, CHARSET);
    }
}
