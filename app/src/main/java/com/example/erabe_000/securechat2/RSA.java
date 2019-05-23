
package com.example.erabe_000.securechat2;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RSA {

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024, new SecureRandom());
            KeyPair pair = generator.generateKeyPair();
            return pair;
        }
        catch (Exception e) {

        }
        return null;
    }


    public static String encrypt(String plainText, PublicKey publicKey) {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(Charset.forName("UTF-8")));

            return android.util.Base64.encodeToString(cipherText, android.util.Base64.DEFAULT);
        } catch (Exception ex){

        }
        return null;
    }

    public static String decrypt(String cipherText, PrivateKey privateKey){
        try {

            byte[] bytes = android.util.Base64.decode(cipherText, android.util.Base64.DEFAULT);

            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

            return new String(decriptCipher.doFinal(bytes), Charset.forName("UTF-8")); //UTF_8
        } catch (Exception ex) {

        }
        return null;
    }

}