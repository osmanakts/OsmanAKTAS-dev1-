package com.eticaret.stajflo.dto;

// İstek verileri için herhangi bir Validation kısıtlaması eklenmemiştir.
// İstenirse @NotBlank veya @Size eklenebilir.

public class AuthRequest {

    private String username;
    private String password;

    // Parametresiz Kurucu (No-Args Constructor)
    public AuthRequest() {
    }

    // Tam Kurucu (All-Args Constructor)
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter Metotları
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // Setter Metotları
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}