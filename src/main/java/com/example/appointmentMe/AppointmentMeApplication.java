package com.example.appointmentMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppointmentMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentMeApplication.class, args);
	}

}
