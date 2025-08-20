package com.example.messenger.service;

import com.example.messenger.crypto.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class PasswordEncryptionService implements PasswordEncoder {

    private final CryptoService cryptoService;

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        byte[] passwordBytes = rawPassword.toString().getBytes(StandardCharsets.UTF_8);
        return cryptoService.encryptToBase64(passwordBytes);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        try {
            byte[] decryptedBytes = cryptoService.decryptFromBase64(encodedPassword);
            String decryptedPassword = new String(decryptedBytes, StandardCharsets.UTF_8);
            return rawPassword.toString().equals(decryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
