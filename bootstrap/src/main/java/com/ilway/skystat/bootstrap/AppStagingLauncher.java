package com.ilway.skystat.bootstrap;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class AppStagingLauncher {
	public static void main(String[] args) {
		new SpringApplicationBuilder(AppConfig.class)
			.profiles("staging")
			.run(args);
	}
}
