package dev.str.electricsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan("dev.str.electricsim")
@EnableScheduling


public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
