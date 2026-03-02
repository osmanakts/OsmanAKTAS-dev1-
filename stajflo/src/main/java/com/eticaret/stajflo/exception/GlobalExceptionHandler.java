package com.eticaret.stajflo.exception;

import com.eticaret.stajflo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice // Tüm Controller'lardaki exception'ları dinler.
public class GlobalExceptionHandler {

    // --- 1. KAYNAK BULUNAMADI HATASI (404 Not Found) ---
    // ResourceNotFoundException daha önce oluşturduğumuz özel exception.
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Bu anatasyon sayesinde @ResourceNotFoundException sınıfındaki @ResponseStatus gerekli kalmayabilir ama güvenlik için tutulabilir.
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage() // Exception'ın fırlatıldığı mesaj
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // --- 2. VERİ DOĞRULAMA HATALARI (400 Bad Request) ---
    // @Valid anotasyonu başarısız olduğunda fırlatılan standart Spring hatası.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Hata alanlarını ve mesajlarını toplamak için Map kullanılır
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(), // Hata veren alan (Örn: "name")
                        fieldError -> fieldError.getDefaultMessage() // Hata mesajı (Örn: "Ürün adı boş olamaz.")
                ));

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Doğrulama hatası oluştu. Lütfen alanları kontrol edin.",
                errors // Toplanan alan bazlı hataları DTO'ya ekler
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // --- 3. GENEL HATA YAKALAYICI (500 Internal Server Error) ---
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Beklenmeyen bir hata oluştu: " + ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}