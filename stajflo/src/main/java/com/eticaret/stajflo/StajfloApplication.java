package com.eticaret.stajflo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.eticaret.stajflo.repository") // Repository'lerin bulunduğu paket
public class StajfloApplication {
	public static void main(String[] args) {
		SpringApplication.run(StajfloApplication.class, args);
	}
}