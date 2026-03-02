package com.eticaret.stajflo.controller;

import com.eticaret.stajflo.dto.ProductRequest;
import com.eticaret.stajflo.dto.ProductResponse;
import com.eticaret.stajflo.exception.ResourceNotFoundException;
import com.eticaret.stajflo.model.Product;
import com.eticaret.stajflo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- 1. Ürün Oluşturma (POST) - ADMIN ONLY ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {

        // DTO -> Entity
        Product product = productService.convertToEntity(productRequest);
        Product savedProduct = productService.save(product);

        // Entity -> DTO
        ProductResponse responseDto = productService.convertToDto(savedProduct);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // --- 2. Tüm Ürünleri Listeleme (GET) - SAYFALANDIRMALI VE FİLTRELİ ---
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            // Sayfalandırma ve Sıralama
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
            // Filtreleme Parametreleri
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") double minPrice,
            @RequestParam(required = false, defaultValue = "9999999") double maxPrice
    ) {
        // Service katmanında sayfalandırma, sıralama ve filtreleme birleştirilir
        Page<ProductResponse> productPage = productService.findAllFiltered(pageable, keyword, minPrice, maxPrice);

        return ResponseEntity.ok(productPage);
    }

    // --- 3. Ürün Detayını Getirme (GET by ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {

        Product product = productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + id));

        ProductResponse responseDto = productService.convertToDto(product);

        return ResponseEntity.ok(responseDto);
    }

    // --- 4. Ürünü Güncelleme (PUT) - ADMIN ONLY ---
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest productRequest) {

        Product existingProduct = productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Güncellenecek ürün bulunamadı: " + id));

        // DTO'dan gelen veriyi mevcut Entity üzerine map et
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());

        ProductResponse responseDto = productService.convertToDto(productService.save(existingProduct));

        return ResponseEntity.ok(responseDto);
    }

    // --- 5. Ürünü Silme (DELETE) - ADMIN ONLY ---
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        if (productService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Silinecek ürün bulunamadı: " + id);
        }

        productService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}