package com.mkyong.http;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import static java.util.Base64.getEncoder;

public class RSAUtil {

    private static String publicKey = "";
    private static String privateKey = "";

    public static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey = null;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
    public String getPublicKey(){
        return this.publicKey;
    }

    public String getPrivateKey(){
        return this.privateKey;
    }

    public static String encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public void keyCheck() {
        File inFile = new File("RSA/publicKey");
        while (publicKey.equals("")) {
            if (inFile.exists()) {
                System.out.println("Using existing file");
                byte[] temp = readContentIntoByteArray(inFile);
                publicKey = getEncoder().encodeToString(temp);
                //System.out.println("Public Key: " + publicKey);
            } else {
                System.out.println("Making new key");

                RSAKeyPairGenerator keyGen = null;
                try {
                    keyGen = new RSAKeyPairGenerator();
                    keyGen.generateKeys();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        inFile = new File("RSA/privateKey");

        if (inFile.exists()) {
            byte[] temp = readContentIntoByteArray(inFile);
            privateKey = getEncoder().encodeToString(temp);
            //System.out.println("Private Key: " + privateKey);
        }

    }

    private static byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        RSAUtil keycheck = new RSAUtil();
        keycheck.keyCheck();
        try {
            String encryptedString = RSAUtil.encrypt("This is my test", publicKey);
            System.out.println(encryptedString);
            String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }
}


