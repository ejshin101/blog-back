package com.example.blog.web.utils;

import com.example.blog.web.prop.WebConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

//참고한 블로그
//https://aday7.tistory.com/entry/Java-AES-%EC%95%94%ED%98%B8%ED%99%94%EB%B3%B5%ED%98%B8%ED%99%94-%EC%98%88%EC%A0%9C-AES-256
@RequiredArgsConstructor
public class AesUtil {
    private static String privateKey_256 = WebConstants.PRIVATE_KEY;

    public static String aesEncoder(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey_256.substring(0,16).getBytes());
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secretKey, IV);
        byte[] encryptionByte = c.doFinal(plainText.getBytes("UTF-8"));
        return Hex.encodeHexString(encryptionByte);
    }

    public static String aesDecoder(String encodedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey_256.substring(0,16).getBytes());
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secretKey, IV);
        System.out.println(encodedText.length());
        byte[] decodeByte = Hex.decodeHex(encodedText.toCharArray());
        byte[] decryptedBytes = c.doFinal(decodeByte);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
