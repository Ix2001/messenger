package com.example.messenger.service;

import com.example.messenger.domain.User;
import com.example.messenger.dto.RegisterRequest;
import com.example.messenger.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Transactional
    public User register(RegisterRequest req) {
        if (users.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username already exists: " + req.username());
        }
        User u = User.builder()
                .username(req.username())
                .displayName(req.displayName())
                .passwordHash(encoder.encode(req.password()))
                .build();
        return users.save(u);
    }

    public List<User> all() { return users.findAll(); }

    public User get(UUID id) {
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public User getByUsername(String username) {
        return users.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
}
