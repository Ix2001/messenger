package com.example.messenger.crypto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoService {

    private final CryptoProperties properties;
    private SecretKey key;
    private final SecureRandom random = new SecureRandom();

    // AES-GCM: IV 12 байт, TAG 16 байт
    public static final String TRANSFORMATION = "AES/GCM/NoPadding";
    public static final int IV_LEN = 12;
    public static final int TAG_BITS = 128;

    @PostConstruct
    void init() throws Exception {
        String base64 = properties.getKeyBase64();
        if (base64 == null || base64.isBlank()) {
            throw new IllegalStateException("APP_CRYPTO_KEY_BASE64 not set");
        }
        byte[] raw = Base64.getDecoder().decode(base64);
        if (raw.length != 32) { // 256-bit
            throw new IllegalStateException("Crypto key must be 32 bytes (Base64 of 32)");
        }
        this.key = new SecretKeySpec(raw, "AES");
        log.info("CryptoService initialized (AES-256-GCM).");
    }

    /**
     * Шифрует plaintext и возвращает Base64(iv || ciphertext+tag).
     */
    public String encryptToBase64(byte[] plaintext) {
        try {
            byte[] iv = new byte[IV_LEN];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] cipherBytes = cipher.doFinal(plaintext);

            byte[] out = new byte[iv.length + cipherBytes.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(cipherBytes, 0, out, iv.length, cipherBytes.length);

            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("Encrypt failed", e);
        }
    }

    /**
     * Принимает Base64(iv || ciphertext+tag), возвращает plaintext.
     */
    public byte[] decryptFromBase64(String base64) {
        try {
            byte[] in = Base64.getDecoder().decode(base64);
            if (in.length < IV_LEN + 16) { // iv + минимум (tag)
                throw new IllegalArgumentException("Ciphertext too short");
            }
            byte[] iv = new byte[IV_LEN];
            System.arraycopy(in, 0, iv, 0, IV_LEN);

            byte[] cipherBytes = new byte[in.length - IV_LEN];
            System.arraycopy(in, IV_LEN, cipherBytes, 0, cipherBytes.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            return cipher.doFinal(cipherBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Decrypt failed", e);
        }
    }
}
