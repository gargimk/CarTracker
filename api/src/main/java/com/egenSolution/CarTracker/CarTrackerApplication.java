package com.egenSolution.CarTracker;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import com.egenSolution.CarTracker.modules.Vehicle;

@SpringBootApplication
public class CarTrackerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CarTrackerApplication.class, args);
		
/*		 RestTemplate restTemplate = new RestTemplate();
         Vehicle vehicle = restTemplate.put("http://localhost:8080/vehicles");
         System.out.println(vehicle.toString());*/
        
	}
}
