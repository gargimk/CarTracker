package com.egenSolution.CarTracker.modules;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "vehicles")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle {
	
	public Vehicle() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Id
	String vin;
	String make;
	String model;
	int year;
	int readlineRpm;
	int maxFuelVolume;
	Date lastServiceDate;
	int highAlert;
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getReadlineRpm() {
		return readlineRpm;
	}
	public void setReadlineRpm(int readlineRpm) {
		this.readlineRpm = readlineRpm;
	}
	public int getMaxFuelVolume() {
		return maxFuelVolume;
	}
	public void setMaxFuelVolume(int maxFuelVolume) {
		this.maxFuelVolume = maxFuelVolume;
	}
	public Date getLastServiceDate() {
		return lastServiceDate;
	}
	public void setLastServiceDate(Date lastServiceDate) {
		this.lastServiceDate = lastServiceDate;
	}
	public int getHighAlert() {
		return highAlert;
	}
	public void setHighAlert(int highAlert) {
		this.highAlert = highAlert;
	}
	@Override
	public String toString() {
		return "Vehicle [vin=" + vin + ", make=" + make + ", model=" + model + ", year=" + year + ", redlineRpm="
				+ readlineRpm + ", maxFuelVolume=" + maxFuelVolume + ", lastServiceDate=" + lastServiceDate + "]";
	}
	
	

}
