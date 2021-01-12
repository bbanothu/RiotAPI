package com.server.main;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.server.main.Requests.AutoRequest;

@SpringBootApplication
public class ServerApplication {
	
	@Autowired


	public static void main(String[] args) throws Exception {
		SpringApplication.run(ServerApplication.class, args);
		AutoRequest app = new AutoRequest();
		app.run(); 
		
	}
}
