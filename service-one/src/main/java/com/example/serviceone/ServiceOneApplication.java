package com.example.serviceone;

import com.example.transactionalbox.constant.EventStatusEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceOneApplication {

	public static void main(String[] args) {
		EventStatusEnum e = EventStatusEnum.FAILED;
		SpringApplication.run(ServiceOneApplication.class, args);
	}

}
