package com.server.main.controller;

import java.util.HashMap;
import java.util.Map.Entry;



public class User {
	
	class userPair {
		String name;
		int wins;
		
		public userPair(String name, int highestWins) {
			this.name = name;
			this.wins = highestWins;
		}
	}

	String username;
	String accountId;
	int totalWins;
	int totalLosses;

	userPair mostWinsWithPlayer;
	userPair mostLossesWithPlayer;
	
	HashMap<String, champStats> myChampStats = new HashMap<String, champStats>();
	HashMap<String, Integer> winsWithPlayer = new HashMap<String, Integer>();
	HashMap<String, Integer> lossesWithPlayer = new HashMap<String, Integer>();

	public User(String accountId, String username) {
		this.accountId = accountId;
		this.username = username;
		this.totalWins = 0;
		this.totalLosses = 0;
	}

	/**
	 * Checks if the champion is already in the list of champ stats
	 * 
	 * @param thisChamp
	 * @return
	 */
	public boolean contains(String thisChamp) {
		return myChampStats.containsKey(thisChamp);
	}

	/**
	 * Adds a champion to list and/or increments the wins of champ
	 * 
	 * @param champName
	 */
	public void addChampWins(String champName) {
		if (contains(champName)) {
			champStats temp = myChampStats.get(champName);
			temp.wins++;
			myChampStats.put(champName, temp);
		} else {
			champStats temp = new champStats(champName);
			temp.wins++;
			myChampStats.put(champName, temp);
		}
	}

	/**
	 * Adds a champion to list and/or increments the losses of champ
	 * 
	 * @param champName
	 */
	public void addChampLosses(String champName) {
		if (contains(champName)) {
			champStats temp = myChampStats.get(champName);
			temp.losses++;
			myChampStats.put(champName, temp);
		} else {
			champStats temp = new champStats(champName);
			temp.losses++;
			myChampStats.put(champName, temp);
		}
	}

	/**
	 * To String function for better viewing in terminal
	 */
	public String toString() {
		return "Wins: " + totalWins + ", Losses: " + totalLosses + ", Champ Stats: " + ChampStatsToString();
	}

	/**
	 * To String function for champStats
	 */
	public String ChampStatsToString() {
		String returnVal = "";
		for (Entry<String, champStats> entry : myChampStats.entrySet()) {
			champStats temp = (champStats) entry.getValue();
			String tempString = "|Name: " + temp.champName + ", Wins: " + temp.wins + ", Losses:" + temp.losses + "|";
			returnVal += tempString;
		}
		return returnVal;
	}

	/**
	 * Checks if the player is already in the list of winners
	 * 
	 * @param thisChamp
	 * @return
	 */
	public boolean containsWinner(String userName) {
		return winsWithPlayer.containsKey(userName);
	}

	/**
	 * Checks if the player is already in the list of loosers
	 * 
	 * @param thisChamp
	 * @return
	 */
	public boolean containsLooser(String userName) {
		return lossesWithPlayer.containsKey(userName);
	}

	/**
	 * Adds a player to list and/or increments the losses of player
	 * 
	 * @param champName
	 */
	public void addPlayer(String userName, boolean trueOrFalse) {
		if (trueOrFalse) {
			if (containsWinner(userName)) {
				int temp = winsWithPlayer.get(userName);
				temp++;
				winsWithPlayer.put(userName, temp);
			} else {
				int temp = 1;
				winsWithPlayer.put(userName, temp);
			}

		} else {
			if (containsLooser(userName)) {
				int temp = lossesWithPlayer.get(userName);
				temp++;
				lossesWithPlayer.put(userName, temp);
			} else {
				int temp = 1;
				lossesWithPlayer.put(userName, temp);
			}

		}

	}
	

	
	public void getMostWins() {
		String highestWinsName = "";
		int highestWins = Integer.MIN_VALUE;

		for (Entry<String, Integer> entry : winsWithPlayer.entrySet()) {
			if (highestWins < entry.getValue()) {
				highestWins = entry.getValue();
				highestWinsName = entry.getKey();
			}
		}

		mostWinsWithPlayer = new userPair(highestWinsName , highestWins);
	}
	
	public void  getMostLosses() {
		String highestLossesName = "";
		int highestLosses = Integer.MIN_VALUE;
		for (Entry<String, Integer> entry : lossesWithPlayer.entrySet()) {
			if (highestLosses < entry.getValue()) {
				highestLosses = entry.getValue();
				highestLossesName = entry.getKey();
			}
		}
		mostLossesWithPlayer = new userPair(highestLossesName , highestLosses);
	}

}
