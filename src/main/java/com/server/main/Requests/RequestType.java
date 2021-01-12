package com.server.main.Requests;

public class RequestType {
	
	public String unit;
	public String passcode;
	public String reservation;
	public String time;
	public String date;
	public String policy;
	public String name;
	public String tel;
	public String email;
	public RequestType(String unit, String passcode, String reservation, String time, String date, String policy, String name, String tel, String email) {
		this.unit = unit;
		this.passcode = passcode;
		this.reservation = reservation;
		this.time = time;
		this.date = date;
		this.policy = policy;
		this.name = name;
		this.tel = tel;
		this.email = email;
		
	
	}
	
}