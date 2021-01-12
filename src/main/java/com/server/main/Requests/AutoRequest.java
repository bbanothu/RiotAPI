package com.server.main.Requests;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Initializes a ranked game between two players after they've both selected this as a game option.
 * @author Kienan bbanothu
 *
 */
public class AutoRequest extends Thread{

	/**
	 * Waiting list for people in rank 1
	 */
	public static SingletonList requestQueue = SingletonList.getInstance();


	public void run()  {
		System.out.println("Request Server is running!");
		try {
			while (true) {
				if (requestQueue.requests.size() > 0) {
					// Compare dates
					if(compareTime(requestQueue.requests.get(0))){
						System.out.println("Making Request");
						MakeRequest(requestQueue.requests.get(0));
						requestQueue.requests.remove(0);
					}

				} else {
					
				}
			}
		} catch (IOException | URISyntaxException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("A ranked game has ended.");
		}
	}
	
	/**
	 * Checks to see if the latest request is less than 2 weeks in advance
	 * @param current
	 * @return
	 */
	public boolean compareTime(RequestType current) {
		LocalDateTime now = LocalDateTime.now().plusWeeks(2);  
		RequestType temp = requestQueue.requests.get(0);
		String str = temp.date + " " + temp.time +":01";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
		if(now.isAfter(dateTime)){
			return true;
		}
		return false;
	}
	
	/**
	 * Makes the request to get the token and the request to make the reservation
	 * @param current
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 */
	public void MakeRequest(RequestType current) throws IOException, URISyntaxException, InterruptedException {
		String token = tokenRequest(current);
		if(!token.equals("false")) {
			String response = reservationRequest(current, token);
		} else {
			// TODO
			System.out.println("error");
		}
	}
	
	/**
	 * Makes the reservation request with the token
	 * @param current
	 * @param token
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String reservationRequest(RequestType current, String token) throws IOException, InterruptedException {
		String viewpoint = Instant.now().toString(); 
		String scope = current.unit;
		String subject = current.reservation;

		int timeValue = Integer.parseInt(current.time);
	
		String urlTimeEnd = "";
		
		if(timeValue+1 < 10) {
			urlTimeEnd = "0" + (timeValue+1);
		} else {
			urlTimeEnd += (timeValue+1);
		}
		 
		String valid = current.date + "T" +  current.time +"%3A00%3A00.000-08%3A00%2F" + current.date + "T" + urlTimeEnd + "%3A00%3A00.000-08%3A00";
		String urlRequest = "https://d1va2v45wuqutp.cloudfront.net/v1/permits/temporary?viewpoint=" + viewpoint + 
				"&issue=true&location="+ scope + "&policy=" + current.policy +"&valid="+ valid  + 
					"&unit=" + subject + "&notes="+ current.name +"&tel="+ current.tel +"&email="+ current.email +"&agreement-bgwvc76te529hcd9qy468e38xm=true&Authorization=bearer+" + token;
			
	    ObjectMapper objectMapper = new ObjectMapper();
	    String requestBody = objectMapper
	                .writeValueAsString("");
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(urlRequest))
	                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	                .build();
		HttpResponse<String> response = client.send(request,
	                HttpResponse.BodyHandlers.ofString());

	        
		JSONObject jsonValue = null;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(response.body());
			jsonValue = (JSONObject) obj;
			System.out.println(jsonValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonValue.toJSONString();
	}
	
	/**
	 * Gets the token for the user with the current request 
	 * @param current
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String tokenRequest(RequestType current) throws IOException, InterruptedException {
		String urlRequest = "https://d1va2v45wuqutp.cloudfront.net/v1/accounts/auth/tokens?";
		String viewpoint = Instant.now().toString(); 
		String scope = current.unit;
		String subject = current.reservation;
		//String subject = "s1ardfc26h1f92qnsvcddqm7j8";
		String password = current.passcode;

		
		// build url
		urlRequest += "viewpoint=" + viewpoint + "&" + "scope=" + scope + "&" + "subject=" + subject + "&" + "password=" + password;
		// Post Request
	    ObjectMapper objectMapper = new ObjectMapper();
	    String requestBody = objectMapper
	                .writeValueAsString("");
	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(urlRequest))
	                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	                .build();
	    HttpResponse<String> response = client.send(request,
	                HttpResponse.BodyHandlers.ofString());

	        
	    JSONObject jsonValue = null;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(response.body());
			jsonValue = (JSONObject) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		if(jsonValue.containsKey("token")) {
			return jsonValue.get("token").toString();
		} else {
			return "false";
		}
	}
	
}
