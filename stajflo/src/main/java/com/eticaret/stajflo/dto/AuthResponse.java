package com.eticaret.stajflo.dto;

public class AuthResponse {

    private String token;
    // Frontend'e username (kullanıcı adı) bilgisini taşır
    private String username;
    // KRİTİK ALAN: Frontend'in yetki kontrolü için gerekli olan rol bilgisini taşır
    private String role;

    // Parametresiz Kurucu (No-Args Constructor)
    public AuthResponse() {
    }

    // Tam Kurucu (All-Args Constructor)
    // Yeni 'role' alanı eklendi
    public AuthResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    // Getter Metotları
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
    public String getRole() { // YENİ GETTER
        return role;
    }

    // Setter Metotları
    public void setToken(String token) {
        this.token = token;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRole(String role) { // YENİ SETTER
        this.role = role;
    }
}