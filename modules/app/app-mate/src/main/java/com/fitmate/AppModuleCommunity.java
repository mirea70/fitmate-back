package com.fitmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AppModuleCommunity {
    public static void main(String[] args) {
            SpringApplication.run(AppModuleCommunity.class, args);
    }
}
