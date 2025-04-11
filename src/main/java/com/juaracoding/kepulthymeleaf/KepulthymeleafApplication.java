package com.juaracoding.kepulthymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KepulthymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(KepulthymeleafApplication.class, args);
	}

}
