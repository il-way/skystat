package com.ilway.skystat.bootstrap;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class AppProductionLauncher {
	public static void main(String[] args) {
		new SpringApplicationBuilder(AppConfig.class)
			.profiles("production")
			.run(args);
	}
}
