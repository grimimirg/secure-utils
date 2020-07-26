package it.grimiandr.security.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author andre
 */
public class CryptoUtil {

    /**
     *
     */
    private String key;

    /**
     *
     */
    private String alg;

    /**
     *
     */
    private String cipher;

    /**
     *
     * @param keyString
     * @param alg
     * @param cipher
     * @return
     * @throws Exception
     */
    public CryptoUtil setUp(String keyString, String alg, String cipher) throws Exception {
        this.key = keyString;
        this.alg = alg;
        this.cipher = cipher;
        return this;
    }

    /**
     *
     * @param input
     * @return
     * @throws Exception
     */
    public byte[] encrypt(String input) throws Exception {
        byte[] keyBytes = this.key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, this.alg);
        Cipher cipher = Cipher.getInstance(this.cipher);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        return cipher.doFinal(inputBytes);
    }

    /**
     *
     * @param input
     * @return
     * @throws Exception
     */
    public String decrypt(byte[] input) throws Exception {
        byte[] keyBytes = this.key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, this.alg);
        Cipher cipher = Cipher.getInstance(this.cipher);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        byte[] inputDecryptedBytes = cipher.doFinal(input);
        return new String(inputDecryptedBytes, StandardCharsets.UTF_8);
    }

}
