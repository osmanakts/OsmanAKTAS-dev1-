package com.eticaret.stajflo.exception; // Yeni exception paketiniz

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Spring'e bu istisna fırlatıldığında HTTP 404 durum kodu döndürmesini söyler.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // İsteğe bağlı, varsayılan bir seri kimliği
    private static final long serialVersionUID = 1L;

    // Constructor: Hatanın mesajını alır (Örn: "Ürün bulunamadı: 5")
    public ResourceNotFoundException(String message) {
        super(message);
    }
}