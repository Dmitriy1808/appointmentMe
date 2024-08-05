package ru.appointme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppointMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointMeApplication.class, args);
	}

}
