package com.indra.bankingstarter.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    private static final String ALGORITHM = "AES";
    private final SecretKey secretKey;

    public EncryptionServiceImpl() throws Exception {
        this.secretKey = KeyGenerator.getInstance(ALGORITHM).generateKey();
    }

    @Override
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting", e);
        }
    }
}
