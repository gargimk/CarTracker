package com.egenSolution.CarTracker;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.egenSolution.CarTracker.database.DatabaseService;
import com.egenSolution.CarTracker.modules.Alert;
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
	
	@RequestMapping(value="/vehicles", method = RequestMethod.GET)
	public @ResponseBody List<Vehicle> getAllVehiclesDetails(){
		return dbService.getAllVehiclesDetails();
	}
	
	@RequestMapping(value="/alerts/{id}", method = RequestMethod.GET)
	public @ResponseBody List<Alert> getAllAlertsForVehicle(@PathVariable(value="id") String vin){
		return dbService.getAllAlertsForVehicle(vin);
	}
	
	@RequestMapping(value="/readings/{hr}/{vin}", method = RequestMethod.GET)
	public @ResponseBody List<Readings> getReadingsData(@PathVariable(value="hr") String hr, @PathVariable(value="vin") String vin){
		return dbService.getReadingsData(hr,vin);
	}
}
