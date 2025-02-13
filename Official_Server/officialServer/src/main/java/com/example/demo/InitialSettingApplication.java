package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
public class InitialSettingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InitialSettingApplication.class, args);
	}

}
