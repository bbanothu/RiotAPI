package com.server.main.Requests;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Java program implementing Singleton class 
// with getInstance() method 
public class SingletonList 
{ 
    // static variable single_instance of type Singleton 
    private static SingletonList instance = null; 
  
    // variable of type String 
    public ArrayList<RequestType> requests; 
  
    // private constructor restricted to this class itself 
    private SingletonList() 
    { 
    	requests = new ArrayList<RequestType>();
    } 
  
    // static method to create instance of Singleton class 
    public static SingletonList getInstance() 
    { 
        if (instance == null) 
        	instance = new SingletonList(); 
        return instance; 
    } 
    
    
    // adds the request to the list and sorts it
    public boolean addRequest(RequestType temp) {
    	System.out.println(temp.date + "|" + temp.time);
    	if(!checkContainsRequest(temp)) {
    		requests.add(temp);
        	Collections.sort(requests, new Comparator<RequestType>(){
        	    public int compare(RequestType s1,RequestType s2) {
        	    	
        	    	
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					
					String s1Time = s1.date + " " + s1.time +":01";
					String s2Time = s2.date + " " + s2.time +":01";
					LocalDateTime dateTimeS1 = LocalDateTime.parse(s1Time, formatter);
					LocalDateTime dateTimeS2 = LocalDateTime.parse(s2Time, formatter);
					
					if(dateTimeS1.isBefore(dateTimeS2)) {
						return -1;
					} else if (dateTimeS1.equals(dateTimeS2)) {
						return 0;
					} else {
						return 1;
					}
        	    }
        	});
        	return true;
    	}
    	return false;
    }
    
    // checks if the request already exists
    public boolean checkContainsRequest(RequestType temp) {
    	for(int i = 0; i<requests.size(); i++) {
    		RequestType current = requests.get(i); 
    		if(temp.unit.equals(current.unit) &&  temp.reservation.equals(current.reservation) &&  temp.time.equals(current.time)  ) {
    			return true;
    		}
    	}
    	return false; 
    }
  
} 
  