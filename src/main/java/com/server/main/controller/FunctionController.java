package com.server.main.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

/**
 * Controller class for using information from the server and modifying it
 * @author bbanothu
 *
 */
 
@RestController
public class FunctionController {
	
	ArrayList<String> customGameIds = new ArrayList<String>(
		List.of("3432745301", "3432745215", "3432545654", "3432894662",
		"3432600156", "3432600019", "3430349368", 
		"3423692071", "3423342521", "3423471962", "3423501855", "3423501596", "3409201018",
		"3404332074", "3404331677", "3402197054", "3402185963"));
    //42.335190,-83.049190
    
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "/test1" , method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public String json1() {
    	// Read Json File
    	
    	HashMap<String, User> allUsers = new HashMap<String, User>(); 
    	JSONArray allCustomGamesJson = null; 
		JSONParser parser = new JSONParser();
		try {
			String path = new File(".").getCanonicalPath() + "\\DataJson.json";
			Object obj = parser.parse(new FileReader(path));
			// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
			 allCustomGamesJson = (JSONArray) obj;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	// get all JsonObjects for custom games
    	ArrayList<JSONObject> allCustomGames = new ArrayList<JSONObject>();
    	for(int i = 0; i<allCustomGamesJson.size(); i++) {
    		if(allCustomGamesJson.get(i) != null) {
                allCustomGames.add((JSONObject) allCustomGamesJson.get(i));
    		}
    	}
    	
    	// iterate through all custom games
    	HashMap<String, JSONObject> allPlayer = new HashMap<String,JSONObject>();
    	for(int i = 0; i<allCustomGames.size(); i++) {
    		JSONObject currentGame = allCustomGames.get(i);
    		JSONArray teams = (JSONArray) currentGame.get("teams");
    		HashMap<String, String> teamWinloss = new HashMap<String, String>();
    		
    		// get teams win / loss
    		JSONObject team1 = (JSONObject) teams.get(0);
    		JSONObject team2 = (JSONObject) teams.get(1);
    		teamWinloss.put(team1.get("teamId").toString(), team1.get("win").toString());
    		teamWinloss.put(team2.get("teamId").toString(), team2.get("win").toString());
    		System.out.println("100: " + teamWinloss.get("100"));
    		System.out.println("200: " + teamWinloss.get("200"));
    		
    		
    		// Add all player info to a map
    		JSONArray participantsArray = (JSONArray) currentGame.get("participants");
    		HashMap<String, JSONObject> participants = new HashMap<String, JSONObject> (); 
    		for(int j = 0; j<participantsArray.size(); j++) {
    			JSONObject tempuser = (JSONObject) participantsArray.get(j);
    			participants.put(tempuser.get("participantId").toString(),tempuser );
    		}
    	
    		// add specific player info to map
    		JSONArray participantsINFOArray = (JSONArray) currentGame.get("participantIdentities");
    		for(int k = 0; k<participantsINFOArray.size(); k++) {
    			JSONObject currentPlayer = (JSONObject) participantsINFOArray.get(k);
    			JSONObject tempParticipent = participants.get(currentPlayer.get("participantId").toString());
    			tempParticipent.put("playerDetails", currentPlayer.get("player"));
    			participants.put(currentPlayer.get("participantId").toString(),tempParticipent );

    		}

    		
    		// iterate thru platyer info and print
    		for (Entry<String, JSONObject> entry : participants.entrySet()) {
    			JSONObject currentUser = entry.getValue();
    			JSONObject playerDetails = (JSONObject) currentUser.get("playerDetails");
    			String username = playerDetails.get("summonerName").toString();
    			String accountId = playerDetails.get("accountId").toString();
    			
    			System.out.println(username);
    			System.out.println(accountId);
    			if(allUsers.containsKey(username)) {
    				User temp = allUsers.get(username);
    				
    				
    				
    				String winOrLoose = (String) currentUser.get("teamId").toString();
    				String champId = currentUser.get("championId").toString();
    				if(teamWinloss.get(winOrLoose).equals("Win")) {
    					temp.totalWins++;
    					
    						temp.addChampWins(champId);
    					
    				}else {
    					temp.totalLosses++;
    					temp.addChampLosses(champId);
    				}
    				
    				
        			allUsers.put(username , temp);
    				
    				
    			}else {
    				User temp = new User(accountId, username);

    				String winOrLoose = (String) currentUser.get("teamId").toString();
    				String champId = currentUser.get("championId").toString();
    				if(teamWinloss.get(winOrLoose).equals("Win")) {
    					temp.totalWins++;
    					
    						temp.addChampWins(champId);
    					
    				}else {
    					temp.totalLosses++;
    					temp.addChampLosses(champId);
    				}
    				
    				
        			allUsers.put(username , temp);
    			}
    			
    		    System.out.println(entry.getKey() + " = " + entry.getValue());
    		}
    	}
    	

        for (Entry<String, User> entry : allUsers.entrySet())  {
        	
        	User temp = entry.getValue();
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + temp.toString()); 
            
            
    }
        
        
        String object = new Gson().toJson( allUsers ); 
        System.out.println(object);
        
        
        
		return "ayy" ;
    }
	
}