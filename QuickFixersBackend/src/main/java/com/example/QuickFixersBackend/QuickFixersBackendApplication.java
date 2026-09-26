package com.example.QuickFixersBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class QuickFixersBackendApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(QuickFixersBackendApplication.class);
		app.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
			String dbUrl = event.getEnvironment().getProperty("DB_URL");
			String dbUser = event.getEnvironment().getProperty("DB_USERNAME");
			boolean jwtSet = event.getEnvironment().getProperty("JWT_SECRET") != null;
			System.out.println(">>> DIAGNOSTIC DB_URL=[" + dbUrl + "] length="
					+ (dbUrl == null ? "MISSING" : dbUrl.length()));
			System.out.println(">>> DIAGNOSTIC DB_USERNAME=[" + dbUser + "]");
			System.out.println(">>> DIAGNOSTIC JWT_SECRET set=" + jwtSet);
		});
		app.run(args);
	}

}