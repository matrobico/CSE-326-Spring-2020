package Ephemeral;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class RSAKeyPairGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }


    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void generateKeys() throws IOException, NoSuchAlgorithmException {
        this.writeToFile("RSA/publicKey", this.getPublicKey().getEncoded());
        this.writeToFile("RSA/privateKey", this.getPrivateKey().getEncoded());
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
        keyGen.generateKeys();
        System.out.println(Base64.getEncoder().encodeToString(keyGen.getPublicKey().getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(keyGen.getPrivateKey().getEncoded()));
    }
}

