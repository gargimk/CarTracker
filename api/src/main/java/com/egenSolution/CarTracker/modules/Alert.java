package com.egenSolution.CarTracker.modules;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "alerts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Alert {
	
	String vin;
	String rule;
	String priority;
	String message;
	
	public Alert() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
