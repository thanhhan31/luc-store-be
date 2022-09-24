package com.lucistore.lucistorebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LuciStoreBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuciStoreBeApplication.class, args);
	}

}
