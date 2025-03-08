package com.example.happyPet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.happyPet")
@EnableScheduling
@EnableAsync
public class HappyPetApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyPetApplication.class, args);
    }

}
