package com.ilway.skystat.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ilway.skystat")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
