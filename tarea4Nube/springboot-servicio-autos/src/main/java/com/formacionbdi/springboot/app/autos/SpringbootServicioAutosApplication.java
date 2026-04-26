package com.formacionbdi.springboot.app.autos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringbootServicioAutosApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootServicioAutosApplication.class, args);
    }
}
