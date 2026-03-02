package com.eticaret.stajflo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterRequest {

    @NotBlank(message = "Kullanıcı adı boş olamaz.")
    @Size(min = 4, message = "Kullanıcı adı en az 4 karakter olmalıdır.")
    private String username;

    @NotBlank(message = "Şifre boş olamaz.")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır.")
    private String password;

    @Email(message = "Geçerli bir email adresi girin.")
    @NotBlank(message = "Email boş olamaz.")
    private String email;

    private String role; // Kullanıcının rolünü belirtmek için

    // Parametresiz Kurucu (No-Args Constructor)
    public UserRegisterRequest() {
    }

    // Tam Kurucu (All-Args Constructor)
    public UserRegisterRequest(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getter Metotları
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    // Setter Metotları
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
}