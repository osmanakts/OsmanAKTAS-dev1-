package com.eticaret.stajflo.dto;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;

    // Parametresiz Kurucu (No-Args Constructor)
    public ProductResponse() {
    }

    // Tam Kurucu (All-Args Constructor)
    public ProductResponse(Long id, String name, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    // Getter Metotları
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    // Setter Metotları
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}