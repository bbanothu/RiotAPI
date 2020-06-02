package com.server.main.controller;

import java.util.HashMap;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//import com.fasterxml.jackson.annotation.JsonValue;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import java.util.Iterator;

/**
 * Controller class for using information from the server and modifying it
 * 
 * @author bbanothu
 *
 */

@RestController
public class FunctionController {
//	ArrayList<String> customGameIds = new ArrayList<String>(
//		List.of("3432745301", "3432745215", "3432545654", "3432894662",
//		"3432600156", "3432600019", "3430349368", 
//		"3423692071", "3423342521", "3423471962", "3423501855", "3423501596", "3409201018",
//		"3404332074", "3404331677", "3402197054", "3402185963"));
	HashMap<String, User> allUsers = new HashMap<String, User>();
	public static champLookup myChampLookup;

	@ResponseBody
	@RequestMapping(value = "/inhouseStats", method = RequestMethod.GET)
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public String getInHouseStatsFromJSON() {
		// Read Json File
		JSONArray allCustomGamesJson = null;
		allUsers = new HashMap<String, User>();
		try {
			String path = new File(".").getCanonicalPath() + "//DataJson.json";
			//String path = new File(".").getCanonicalPath() + "\\DataJson.json";
			allCustomGamesJson = jsonArrayParser(path);
			myChampLookup = new champLookup();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// iterate through all custom games
		for (int i = 0; i < allCustomGamesJson.size(); i++) {
			JSONObject currentGame = (JSONObject) allCustomGamesJson.get(i);
			JSONArray teams = (JSONArray) currentGame.get("teams");

			// get teams win / loss
			JSONObject team1 = (JSONObject) teams.get(0);
			JSONObject team2 = (JSONObject) teams.get(1);

			HashMap<String, String> teamWinloss = new HashMap<String, String>();
			teamWinloss.put(team1.get("teamId").toString(), team1.get("win").toString());
			teamWinloss.put(team2.get("teamId").toString(), team2.get("win").toString());

			// Add all player info to a map and then add specific player info to that map
			HashMap<String, JSONObject> participants = getParticipantsMap(currentGame);
			addToParticipantsMap(currentGame, participants);

			// Format data and create the return value
			formatData(participants, allUsers, teamWinloss);
		}

		String returnValue = new Gson().toJson(allUsers);
		
		//JsonObject temp = new Gson().toJson(allUsers);
		System.out.println(returnValue);
		return returnValue;
	}

	/**
	 * Adds participants to allUsers and increments their respective win/losses for
	 * user and champstats
	 * 
	 * @param participants
	 * @param allUsers
	 * @param teamWinloss
	 */
	public void formatData(HashMap<String, JSONObject> participants, HashMap<String, User> allUsers,
			HashMap<String, String> teamWinloss) {
		HashMap<String, User> winningTeam = new HashMap<String, User>();
		HashMap<String, User> loosingTeam = new HashMap<String, User>();
		for (Entry<String, JSONObject> entry : participants.entrySet()) {
			JSONObject currentUser = entry.getValue();
			JSONObject playerDetails = (JSONObject) currentUser.get("playerDetails");
			String username = playerDetails.get("summonerName").toString();
			String accountId = playerDetails.get("accountId").toString();

			JSONObject stats = (JSONObject) currentUser.get("stats");
			if (allUsers.containsKey(username)) {
				User temp = allUsers.get(username);
				String winOrLoose = (String) currentUser.get("teamId").toString();
				String champId = currentUser.get("championId").toString();
				if (teamWinloss.get(winOrLoose).equals("Win")) {
					temp.totalWins++;
					temp.addChampWins(champId);
					winningTeam.put(username, temp);
				} else {
					temp.totalLosses++;
					temp.addChampLosses(champId);
					loosingTeam.put(username, temp);
				}
				temp.totalKills += Integer.parseInt( stats.get("kills").toString());
				temp.totalDeaths += Integer.parseInt( stats.get("deaths").toString());
				temp.totalAssists += Integer.parseInt( stats.get("assists").toString());
				temp.totalDamageDealt += Integer.parseInt( stats.get("totalDamageDealtToChampions").toString());
				allUsers.put(username, temp);
			} else {
				User temp = new User(accountId, username);
				String winOrLoose = (String) currentUser.get("teamId").toString();
				String champId = currentUser.get("championId").toString();
				if (teamWinloss.get(winOrLoose).equals("Win")) {
					temp.totalWins++;
					temp.addChampWins(champId);
					winningTeam.put(username, temp);
				} else {
					temp.totalLosses++;
					temp.addChampLosses(champId);
					loosingTeam.put(username, temp);
				}
				allUsers.put(username, temp);
			}
		}
		updateWins(winningTeam, loosingTeam);
		updateMostWinsWithPlayers();
	}
	
	public void updateMostWinsWithPlayers() {
		for (Entry<String, User> entry : allUsers.entrySet()) {
			User temp = allUsers.get(entry.getKey());
			temp.getMostWins();
			temp.getMostLosses();
			temp.mostWinLossPairCalc();
			
			allUsers.put(entry.getKey() , temp);
			
		}
	}

	/**
	 * Helper to update wins and losses with other players
	 * 
	 * @param winningTeam
	 * @param loosingTeam
	 */
	public void updateWins(HashMap<String, User> winningTeam, HashMap<String, User> loosingTeam) {
		for (Entry<String, User> entryLooser : loosingTeam.entrySet()) {
			User currentPlayer = allUsers.get(entryLooser.getKey());
			for (Entry<String, User> entryLooser1 : loosingTeam.entrySet()) {
				User OtherPlayers = allUsers.get(entryLooser1.getKey());
				if (!OtherPlayers.username.equals(currentPlayer.username)) {
					currentPlayer.addPlayer(OtherPlayers.username, false);
				}
			}
			allUsers.put(entryLooser.getKey(), currentPlayer);
		}

		for (Entry<String, User> entryWinner : winningTeam.entrySet()) {
			User currentPlayer = allUsers.get(entryWinner.getKey());
			for (Entry<String, User> entryWinner1 : winningTeam.entrySet()) {
				User OtherPlayers = allUsers.get(entryWinner1.getKey());
				if (!OtherPlayers.username.equals(currentPlayer.username)) {
					currentPlayer.addPlayer(OtherPlayers.username, true);
				}
			}
			allUsers.put(entryWinner.getKey(), currentPlayer);
		}
	}

	/**
	 * Add playerDetails to the participants map
	 * 
	 * @param currentGame
	 * @param participants
	 */
	@SuppressWarnings("unchecked")
	public void addToParticipantsMap(JSONObject currentGame, HashMap<String, JSONObject> participants) {
		JSONArray participantsINFOArray = (JSONArray) currentGame.get("participantIdentities");
		for (int k = 0; k < participantsINFOArray.size(); k++) {
			JSONObject currentPlayer = (JSONObject) participantsINFOArray.get(k);
			JSONObject tempParticipent = participants.get(currentPlayer.get("participantId").toString());
			JSONObject player = (JSONObject) currentPlayer.get("player");
			tempParticipent.put("playerDetails", player);
			participants.put(currentPlayer.get("participantId").toString(), tempParticipent);
		}

	}

	/**
	 * Returns a Map with just the participants Jsons
	 * 
	 * @param currentGame
	 * @return
	 */
	public HashMap<String, JSONObject> getParticipantsMap(JSONObject currentGame) {
		JSONArray participantsArray = (JSONArray) currentGame.get("participants");
		HashMap<String, JSONObject> participants = new HashMap<String, JSONObject>();
		for (int j = 0; j < participantsArray.size(); j++) {
			JSONObject tempuser = (JSONObject) participantsArray.get(j);
			participants.put(tempuser.get("participantId").toString(), tempuser);
		}
		return participants;
	}

	/**
	 * Parse and return a JSONArray from given a path
	 * 
	 * @param path
	 * @return
	 */
	public static JSONArray jsonArrayParser(String path) {
		JSONArray myJsonArray = null;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path));
			myJsonArray = (JSONArray) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myJsonArray;
	}

}