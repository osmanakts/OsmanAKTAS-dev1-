package com.eticaret.stajflo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductRequest {

    @NotBlank(message = "Ürün adı boş bırakılamaz.")
    @Size(min = 3, max = 100, message = "Ürün adı 3 ile 100 karakter arasında olmalıdır.")
    private String name;

    private String description;

    @NotNull(message = "Fiyat boş bırakılamaz.")
    @Min(value = 0, message = "Fiyat sıfırdan küçük olamaz.")
    private double price;

    @NotNull(message = "Stok miktarı boş bırakılamaz.")
    @Min(value = 0, message = "Stok miktarı sıfırdan küçük olamaz.")
    private int stock;

    // Parametresiz Kurucu (No-Args Constructor)
    public ProductRequest() {
    }

    // Tam Kurucu (All-Args Constructor)
    public ProductRequest(String name, String description, double price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    // Getter Metotları
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    // Setter Metotları
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}