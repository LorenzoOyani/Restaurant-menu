package com.example.crud_menu;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class CrudMenuApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(CrudMenuApplication.class, args);
    }


    @PostConstruct
    public void checkActiveProfiles() {
        log.info("Active profiles {}", String.join(",", env.getActiveProfiles()));
    }

}
