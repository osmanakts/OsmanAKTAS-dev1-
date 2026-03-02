package com.eticaret.stajflo.service;

import com.eticaret.stajflo.dto.ProductRequest;
import com.eticaret.stajflo.dto.ProductResponse;
import com.eticaret.stajflo.model.Product;
import com.eticaret.stajflo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- YENİ METOT: Sayfalandırılmış ve Filtrelenmiş Ürünleri Getirme ---
    public Page<ProductResponse> findAllFiltered(Pageable pageable, String keyword, double minPrice, double maxPrice) {

        Page<Product> productPage;

        // Fiyat aralığı varsayılan değerlerden farklı mı?
        boolean isPriceRangeActive = minPrice > 0 || maxPrice < 9999999;
        // Anahtar kelime boş veya null değil mi?
        boolean isKeywordActive = keyword != null && !keyword.trim().isEmpty();

        if (isKeywordActive && isPriceRangeActive) {
            // 1. Hem Anahtar Kelime hem de Fiyat Aralığı
            productPage = productRepository.findByNameContainingIgnoreCaseAndPriceBetween(
                    keyword, minPrice, maxPrice, pageable
            );
        } else if (isKeywordActive) {
            // 2. Sadece Anahtar Kelime (İsim veya açıklama içinde arama)
            productPage = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    keyword, keyword, pageable
            );
        } else if (isPriceRangeActive) {
            // 3. Sadece Fiyat Aralığı
            productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        } else {
            // 4. Filtre yok, sadece Sayfalandırma ve Sıralama
            productPage = productRepository.findAll(pageable);
        }

        // Page<Product>'ı Page<ProductResponse>'a dönüştürülür
        return productPage.map(this::convertToDto);
    }

    // --- TEMEL CRUD METOTLARI ---
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    // --- DTO DÖNÜŞÜM METOTLARI (MAPPING) ---
    public Product convertToEntity(ProductRequest dto) {
        if (dto == null) return null;

        Product entity = new Product();
        // ID'yi manuel set etmeyiz, JPA halleder.
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        return entity;
    }

    public ProductResponse convertToDto(Product entity) {
        if (entity == null) return null;

        ProductResponse dto = new ProductResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        return dto;
    }
}