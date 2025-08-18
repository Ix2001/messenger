package com.example.messenger.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.charset.StandardCharsets;

@Converter(autoApply = false)
public class CryptoStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attributePlain) {
        if (attributePlain == null) return null;
        byte[] bytes = attributePlain.getBytes(StandardCharsets.UTF_8);
        return CryptoServiceHolder.INSTANCE.encryptToBase64(bytes);
    }

    @Override
    public String convertToEntityAttribute(String dbDataCipherBase64) {
        if (dbDataCipherBase64 == null) return null;
        byte[] plain = CryptoServiceHolder.INSTANCE.decryptFromBase64(dbDataCipherBase64);
        return new String(plain, StandardCharsets.UTF_8);
    }
}
