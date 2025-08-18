package com.example.messenger.crypto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptoServiceHolder {
    private final CryptoService cryptoService;
    public static CryptoService INSTANCE;

    @PostConstruct
    void init() {
        INSTANCE = cryptoService;
    }
}
