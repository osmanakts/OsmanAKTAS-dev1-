package com.eticaret.stajflo.service;

import com.eticaret.stajflo.dto.UserRegisterRequest;
import com.eticaret.stajflo.model.User;
import com.eticaret.stajflo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Şifrelemek (hashing) için gerekli

    // Constructor Injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- 1. Kullanıcı Kaydı Metodu (AuthController tarafından çağrılır) ---
    public User registerNewUser(UserRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());

        // KRİTİK: Şifreyi kaydetmeden önce şifrele (hash)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    // --- 2. Kullanıcı Adına Göre Bulma Metodu (Controller ve Security tarafından kullanılır) ---
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Gerekli olabilecek diğer temel CRUD metotları buraya eklenebilir
    // ...
}