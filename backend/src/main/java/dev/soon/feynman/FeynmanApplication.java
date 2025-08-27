package dev.soon.feynman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FeynmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeynmanApplication.class, args);
	}

}
