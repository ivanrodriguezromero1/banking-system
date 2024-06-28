package com.indra.bankingstarter.encryption;

public interface EncryptionService {
    String encrypt(String data);
    String decrypt(String encryptedData);
}