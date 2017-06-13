package com.egenSolution.CarTracker.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.egenSolution.CarTracker.email.EmailService;
import com.egenSolution.CarTracker.modules.Alert;
import com.egenSolution.CarTracker.modules.Readings;
import com.egenSolution.CarTracker.modules.Tires;
import com.egenSolution.CarTracker.modules.Vehicle;


public class DatabaseService {

	ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	JavaMailSender javaMailSender =  new JavaMailSenderImpl();
	EmailService emailService = new EmailService(javaMailSender);

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
			alert.setDate(new Date());
			mongoOperation.save(alert);
			//emailService.sendMail("someemail@gmail.com", "HIGH Alert", "High priority:engineRpm is greater than readLineRpm");
		}
		if (readings.getFuelVolume() < 0.1 * vehicle.getMaxFuelVolume()) {
			alert = new Alert();
			alert.setMessage("Medium priority:fuelVolume is less than 10% of maxFuelVolume");
			alert.setVin(readings.getVin());
			alert.setRule("fuelVolume is less than 10% of maxFuelVolume");
			alert.setPriority("MEDIUM");
			alert.setDate(new Date());
			mongoOperation.save(alert);
		}
		if (isTirePressureOutOfLimit(readings)) {
			alert = new Alert();
			alert.setMessage("Low priority:tire pressure out of limit");
			alert.setVin(readings.getVin());
			alert.setRule("tire pressure of any tire < 32psi or >36psi");
			alert.setPriority("LOW");
			alert.setDate(new Date());
			mongoOperation.save(alert);
		}
		if (readings.isEngineCoolantLow() || readings.isCheckEngineLightOn()) {
			alert = new Alert();
			alert.setMessage("Low priority:engineCoolantLow or engineLightOn");
			alert.setVin(readings.getVin());
			alert.setRule("engineCoolantLow or engineLightOn");
			alert.setPriority("LOW");
			alert.setDate(new Date());
			mongoOperation.save(alert);
		}

	}
	
	public List<Vehicle> getAllVehiclesDetails(){
		return getAllFormattedVehiclesDetails();
	}

	public List<Alert> getAllAlertsForVehicle(String vin) {
		List<Alert> alerts = mongoOperation.find(new Query(Criteria.where("vin").is(vin)),Alert.class);
		return alerts;
	}
	

	public List<Readings> getReadingsData(String hr, String vin) {
		Date now = new Date();
		Date HrsBefore = new Date(now.getTime()-(3600 * Integer.valueOf(hr)*1000));
		List<Readings> readings = mongoOperation.find(new Query(Criteria.where("timestamp").gt(HrsBefore).and("vin").is(vin)),Readings.class);
		return readings;
	}
	
	private List<Vehicle> getAllFormattedVehiclesDetails(){
		List<Vehicle> vehicles = mongoOperation.findAll(Vehicle.class);
		List<Vehicle> output = new ArrayList<Vehicle>();
		Map<String,Vehicle> map = new HashMap<String,Vehicle>();
		for(Vehicle v:vehicles){
			v.setHighAlert(0);
			map.put(v.getVin(), v);
		}
		Date now = new Date();
		Date twoHrsBefore = new Date(now.getTime()-(3600 * 2000));
		List<Alert> alerts = mongoOperation.find(new Query(Criteria.where("date").gt(twoHrsBefore)),Alert.class);
		for(Alert a:alerts){
			
			if(map.containsKey(a.getVin())){
				Vehicle v = map.get(a.getVin());
				if(a.getPriority().equalsIgnoreCase("high")){
					v.setHighAlert(v.getHighAlert()+1);
					map.put(a.getVin(), v);
				}
			}
		}
		output.addAll(map.values());
		return output;
	}
	
	private boolean isTirePressureOutOfLimit(Readings readings){
		Tires tires = readings.getTires();
		return tires.getFrontLeft()<32||tires.getFrontLeft()>36||tires.getFrontRight()<32||tires.getFrontRight()>36||tires.getRearLeft()<32||tires.getRearLeft()>36||tires.getRearRight()<32||tires.getRearRight()>36;
	}



}
