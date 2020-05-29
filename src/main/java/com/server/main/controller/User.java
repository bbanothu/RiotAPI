package com.server.main.controller;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
 
public class User {
	
	String username;
	String accountId;
	int totalWins;
	int totalLosses;
	
	HashMap<String, champStats> myChampStats = new HashMap<String, champStats>(); 
	
	public User(String accountId, String username) {
		this.accountId = accountId;
		this.username = username; 
		this.totalWins = 0;
		this.totalLosses = 0;
	}
	
	
	public boolean contains(String thisChamp) {
		return myChampStats.containsKey(thisChamp);
	}
	
	public void addChampWins(String champName) {
		if(contains(champName)) {
			champStats temp = myChampStats.get(champName);
			temp.wins++;
			myChampStats.put(champName,temp );

		}else {
			champStats temp = new champStats(champName);
			temp.wins++;
			myChampStats.put(champName,temp );
			
		}
		
	}
	
	public void addChampLosses(String champName) {
		if(contains(champName)) {
			champStats temp = myChampStats.get(champName);
			temp.losses++;
			myChampStats.put(champName,temp );

		}else {
			champStats temp = new champStats(champName);
			temp.losses++;
			myChampStats.put(champName,temp );
			
		}
	}

	
	public String toString() {
		return "Wins: " + totalWins + ", Losses: " + totalLosses + ", Champ Stats: " + ChampStatsToString();
	}
	
	public String ChampStatsToString() {
		String returnVal = "";
        for (Entry<String, champStats> entry : myChampStats.entrySet())  {
        	champStats temp = (champStats) entry.getValue();
        	String tempString = "|Name: " + temp.champName + ", Wins: " + temp.wins + ", Losses:" + temp.losses + "|";
        	returnVal += tempString;
        }
		return returnVal;
	}
	

}
