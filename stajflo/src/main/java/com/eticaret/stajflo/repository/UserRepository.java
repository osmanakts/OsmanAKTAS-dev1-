package com.eticaret.stajflo.repository;

import com.eticaret.stajflo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // BU METOT BURAYA AİTTİR:
    Optional<User> findByUsername(String username);
}