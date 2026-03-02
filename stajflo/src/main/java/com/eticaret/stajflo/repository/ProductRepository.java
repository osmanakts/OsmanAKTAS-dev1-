package com.eticaret.stajflo.repository;

import com.eticaret.stajflo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository anotasyonu JpaRepository'den miras aldığı için isteğe bağlıdır,
// ancak eklenmesi sorun yaratmaz.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Filtre: İsim VEYA Açıklama
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String nameKeyword,
            String descriptionKeyword,
            Pageable pageable
    );

    // 2. Filtre: Sadece Fiyat Aralığı
    Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

    // 3. Filtre: İsim ve Fiyat Aralığı
    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(
            String keyword,
            double minPrice,
            double maxPrice,
            Pageable pageable
    );

    // Temel findById, findAll, save vb. metotlar JpaRepository'den gelir.
}