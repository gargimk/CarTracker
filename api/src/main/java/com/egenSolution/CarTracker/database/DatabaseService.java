package com.egenSolution.CarTracker.database;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.egenSolution.CarTracker.modules.Alert;
import com.egenSolution.CarTracker.modules.Readings;
import com.egenSolution.CarTracker.modules.Tires;
import com.egenSolution.CarTracker.modules.Vehicle;

public class DatabaseService {

	ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

	public void saveVehicles(List<Vehicle> vehicleList) {
		for (Vehicle vehicle : vehicleList)
			mongoOperation.save(vehicle);

	}

	public void saveReadings(Readings readings) {
		mongoOperation.save(readings);

	}

	public void generateAlert(Readings readings) {
		Alert alert;
		Vehicle vehicle = mongoOperation.findOne(new Query(Criteria.where("vin").is(readings.getVin())), Vehicle.class);
		if (readings.getEngineRpm() > vehicle.getReadlineRpm()) {
			alert = new Alert();
			alert.setMessage("High priority:engineRpm is greater than readLineRpm");
			alert.setVin(readings.getVin());
			alert.setRule("engineRpm is greater than readLineRpm");
			alert.setPriority("HIGH");
			mongoOperation.save(alert);
		}
		if (readings.getFuelVolume() < 0.1 * vehicle.getMaxFuelVolume()) {
			alert = new Alert();
			alert.setMessage("Medium priority:fuelVolume is less than 10% of maxFuelVolume");
			alert.setVin(readings.getVin());
			alert.setRule("fuelVolume is less than 10% of maxFuelVolume");
			alert.setPriority("MEDIUM");
			mongoOperation.save(alert);
		}
		if (isTirePressureOutOfLimit(readings)) {
			alert = new Alert();
			alert.setMessage("Low priority:tire pressure out of limit");
			alert.setVin(readings.getVin());
			alert.setRule("tire pressure of any tire < 32psi or >36psi");
			alert.setPriority("LOW");
			mongoOperation.save(alert);
		}
		if (readings.isEngineCoolantLow() || readings.isCheckEngineLightOn()) {
			alert = new Alert();
			alert.setMessage("Low priority:engineCoolantLow or engineLightOn");
			alert.setVin(readings.getVin());
			alert.setRule("engineCoolantLow or engineLightOn");
			alert.setPriority("LOW");
			mongoOperation.save(alert);
		}

	}

	public boolean isTirePressureOutOfLimit(Readings readings){
		Tires tires = readings.getTires();
		return tires.getFrontLeft()<32||tires.getFrontLeft()>36||tires.getFrontRight()<32||tires.getFrontRight()>36||tires.getRearLeft()<32||tires.getRearLeft()>36||tires.getRearRight()<32||tires.getRearRight()>36;
	}

}
