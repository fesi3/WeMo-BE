package com.wemo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WeMoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeMoApplication.class, args);
	}

}
