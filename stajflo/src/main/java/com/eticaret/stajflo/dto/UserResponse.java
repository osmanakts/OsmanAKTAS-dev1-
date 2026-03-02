package com.eticaret.stajflo.dto;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String role;

    // Parametresiz Kurucu (No-Args Constructor)
    public UserResponse() {
    }

    // Tam Kurucu (All-Args Constructor)
    public UserResponse(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getter Metotları
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    // Setter Metotları
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
}