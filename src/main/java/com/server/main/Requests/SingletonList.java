package com.server.main.Requests;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Used as a single reusuable store for requests
 * 
 * @author bbanothu
 *
 */
public class SingletonList {
	// static variable single_instance of type Singleton
	private static SingletonList instance = null;

	// Stores all requests
	public ArrayList<RequestType> requests;

	// private constructor restricted to this class itself
	private SingletonList() {
		requests = new ArrayList<RequestType>();
	}

	// static method to create instance of Singleton class
	public static SingletonList getInstance() {
		if (instance == null)
			instance = new SingletonList();
		return instance;
	}

	/**
	 * Adds request to list and sorts it based off time
	 * @param temp
	 * @return
	 */
	public boolean addRequest(RequestType temp) {
		System.out.println(temp.date + "|" + temp.time);
		if (!checkContainsRequest(temp)) {
			requests.add(temp);
			Collections.sort(requests, new Comparator<RequestType>() {
				public int compare(RequestType s1, RequestType s2) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					String s1Time = s1.date + " " + s1.time + ":01";
					String s2Time = s2.date + " " + s2.time + ":01";
					LocalDateTime dateTimeS1 = LocalDateTime.parse(s1Time, formatter);
					LocalDateTime dateTimeS2 = LocalDateTime.parse(s2Time, formatter);

					if (dateTimeS1.isBefore(dateTimeS2)) {
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

	/**
	 * Checks if a similar request already exists
	 * @param temp
	 * @return
	 */
	public boolean checkContainsRequest(RequestType temp) {
		for (int i = 0; i < requests.size(); i++) {
			RequestType current = requests.get(i);
			if (temp.unit.equals(current.unit) && temp.reservation.equals(current.reservation)
					&& temp.time.equals(current.time) && temp.date.equals(current.date)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Used to remove requests
	 * @param current
	 * @return
	 */
	public boolean removeRequest(RequestType current) {
		for (int i = 0; i < requests.size(); i++) {
			RequestType temp = requests.get(i);
			if (temp.unit.equals(current.unit) && temp.passcode.equals(current.passcode)
					&& temp.reservation.equals(current.reservation) && temp.time.equals(current.time)
					&& temp.date.equals(current.date) && temp.policy.equals(current.policy)) {
				requests.remove(i);
				return true;
			}
		}

		return false;
	}

}
