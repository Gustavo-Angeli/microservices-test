package com.gusta.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.*;

@EnableFeignClients
@SpringBootApplication
public class BankApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankApiApplication.class, args);
	}
}
