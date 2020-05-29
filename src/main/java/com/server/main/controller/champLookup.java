package com.server.main.controller;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public  class champLookup {

	public static HashMap<String, String> allChampValues = new HashMap<String, String>();
	public champLookup() {
		initChampLookup();
	}
	
	/**
	 * Inits the allChampValues from Json File
	 */
	@SuppressWarnings("unchecked")
	public void initChampLookup() {
		// Read Json File
		try {
			String path = new File(".").getCanonicalPath() + "\\ChampInfoJson.json";
			JSONObject allChampStats = jsonObjectParser(path);
			JSONObject allChampStatsArray = (JSONObject) allChampStats.get("data");
			allChampStatsArray.keySet().forEach(keyStr ->{
		    	JSONObject champion = (JSONObject) allChampStatsArray.get(keyStr);
		    	allChampValues.put(champion.get("key").toString(),champion.get("id").toString() );  
		    });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String champName(String id) {
		return allChampValues.get(id);
	}
	
    /**
     * Parse and return a JSONObject from given a path
     * @param path
     * @return
     */
    public static JSONObject jsonObjectParser(String path) {
    	JSONObject myJsonArray = null;
    	JSONParser parser = new JSONParser();
    	try {
			Object obj = parser.parse(new FileReader(path));
    		myJsonArray = (JSONObject) obj;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return myJsonArray;
    }
   
}
