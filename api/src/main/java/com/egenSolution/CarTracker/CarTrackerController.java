package com.egenSolution.CarTracker;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.egenSolution.CarTracker.database.DatabaseService;
import com.egenSolution.CarTracker.modules.Readings;
import com.egenSolution.CarTracker.modules.Vehicle;



@RestController
public class CarTrackerController {
	DatabaseService dbService = new DatabaseService();
	@CrossOrigin
	@RequestMapping(value="/vehicles", method = RequestMethod.PUT)
	public void putVehiclesLists(@RequestBody List<Vehicle> vehicleList){
		dbService.saveVehicles(vehicleList);	
	}
	@CrossOrigin
	@RequestMapping(value="/readings", method = RequestMethod.POST)
	public void postReadings(@RequestBody Readings readings){
		dbService.saveReadings(readings);
		dbService.generateAlert(readings);
	}
}
