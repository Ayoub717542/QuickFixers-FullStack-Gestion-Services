package com.example.QuickFixersBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@EnableCaching
@SpringBootApplication
public class QuickFixersBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickFixersBackendApplication.class, args);
	}

}