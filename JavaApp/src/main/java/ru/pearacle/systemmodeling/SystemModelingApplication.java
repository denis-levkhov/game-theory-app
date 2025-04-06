package ru.pearacle.systemmodeling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SystemModelingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemModelingApplication.class, args);
    }
}
