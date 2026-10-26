package com.safo.safoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.safo.safoapi.repository")
@EntityScan(basePackages = "com.safo.safoapi.model")
public class SafoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafoApiApplication.class, args);
    }
}