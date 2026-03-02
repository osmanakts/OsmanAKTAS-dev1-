package com.eticaret.stajflo.controller;

import com.eticaret.stajflo.dto.AuthRequest;
import com.eticaret.stajflo.dto.AuthResponse; // AuthResponse'u rol alanı için güncelledik
import com.eticaret.stajflo.dto.UserRegisterRequest;
import com.eticaret.stajflo.exception.UserAlreadyExistsException;
import com.eticaret.stajflo.model.User;
import com.eticaret.stajflo.security.JwtUtil;
import com.eticaret.stajflo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority; // GrantedAuthority import'u eklendi
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // --- 1. Kullanıcı Kaydı (Register) ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Kullanıcı adı zaten kullanımda: " + request.getUsername());
        }

        User newUser = userService.registerNewUser(request);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // --- 2. Kullanıcı Girişi (Login) - ROL BİLGİSİ EKLENDİ ---
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@Valid @RequestBody AuthRequest authenticationRequest) throws Exception {

        // Spring Security ile kimlik doğrulama işlemi
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // Başarılı olursa, UserDetails objesini al
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // JWT Token oluştur
        final String jwt = jwtUtil.generateToken(userDetails);

        // KRİTİK GÜNCELLEME: Kullanıcının rolünü çekme ve temizleme (ROLE_ADMIN -> ADMIN)
        String userRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER"); // Eğer rol yoksa varsayılan

        // "ROLE_" kısmını atar. Örneğin, "ROLE_ADMIN" -> "ADMIN" olur.
        // Bu, Frontend'deki role === 'ADMIN' kontrolünü kolaylaştırır.
        String simpleRole = userRole.startsWith("ROLE_") ? userRole.substring(5) : userRole;


        // JWT Token'ı, kullanıcı adını VE ROLÜ içeren yanıtı döndür
        return ResponseEntity.ok(new AuthResponse(
                jwt,
                userDetails.getUsername(),
                simpleRole // <-- ROL ARTIK BURADA GÖNDERİLİYOR!
        ));
    }
}