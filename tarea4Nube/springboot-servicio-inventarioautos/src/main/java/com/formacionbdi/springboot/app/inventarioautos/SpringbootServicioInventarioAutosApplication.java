package com.formacionbdi.springboot.app.inventarioautos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@RibbonClient(name = "servicio-autos")
@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker 
@EnableHystrixDashboard  
public class SpringbootServicioInventarioAutosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioInventarioAutosApplication.class, args);
	}
}
