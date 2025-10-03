package com.example.Qolzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.Qolzy.model")
public class QolzyApplication {

	public static void main(String[] args) {
		SpringApplication.run(QolzyApplication.class, args);
	}

}
