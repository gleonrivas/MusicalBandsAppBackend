package com.example.solfamidasback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages={
        "com.example.solfamidasback"})
@ComponentScan("com.example.solfamidasback.model.DTO")
public class SolfamidasBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolfamidasBackApplication.class, args);
    }

}
