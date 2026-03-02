package com.eticaret.stajflo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.AuthenticationEntryPoint;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, AuthenticationEntryPoint unauthorizedHandler) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF ve Frame Option (H2 için) devre dışı
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                // Exception Handling
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(unauthorizedHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        // Login, Register ve Hata yollarına erişim izni
                        .requestMatchers("/api/auth/**", "/error", "/", "/h2-console/**").permitAll()

                        // Ürün listeleme/detay (GET) isteklerine token olmadan erişim izni
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // Ürün EKLEME (POST) isteği sadece ADMIN rolüne sahip kullanıcılara açık
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")

                        // YENİ: Ürün SİLME (DELETE) isteği sadece ADMIN'lere açık olmalı.
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // Geriye kalan tüm isteklere sadece token ile (authenticated) erişim izni
                        .anyRequest().authenticated()
                )
                // Session'sız (Stateless) JWT kullanımı
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // JWT Filtremizi standart Spring Security filtresinden önce ekle
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}