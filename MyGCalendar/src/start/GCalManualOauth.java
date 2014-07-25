/*
 * CS275 : OAuth Lab - Manual
 * Maria Yala
 */
package start;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GCalManualOauth {
	static String access_token = "";
	static String refresh_token = "";
	
	static String authorizationURL = "";
	static String authorization_code = "";
	static String callbackID = "";
	static String tokenRequestURL = "https://accounts.google.com/o/oauth2/token";
	
	static String clientSecret = "T1TkK-L6y83d1RzUuhtLLxzZ";
	static String clientID = "352389790852-79k5qc29hfldc7bjcse75o9bt6mu0m7b.apps.googleusercontent.com";
	static String redirect_uri = "https://www.cs.drexel.edu/~may36/oauth2callback";
	
	static JsonObject rootObject;
	static JsonObject rootObjectTokens;
	static JsonObject rootCalendarList;
	static JsonObject rootCalendarEvents;
	
	// Calendars
	static String calScope = "https://www.googleapis.com/auth/calendar.readonly";
	static String calID = "";
	static String calBase_uri = "https://www.googleapis.com/calendar/v3";
	static String calList = "/users/me/calendarList";
	static String calendar = "/calendars/";
	
	// Google OAuth 2.0 endpoint
	static String endpoint = "https://accounts.google.com/o/oauth2/auth";
	
	// Indicator whether to move onto Google Calendar access
	static Boolean accessCalendars = false;
	
	public static void main(String[] args) throws Exception{
		// Begin initial OAuth step - asking user to click "allow"
		askUserToAuthorizeApp();
		// If OAuth process is complete and an access token is available, then go ahead to Calendars API
		if (accessCalendars){
			callCalendarAPI();
		} else {
			// No access token available. 
			System.out.println("Authentification process failed.");
			System.exit(0);
		}
	}
	
	/**
	 * Send the user to a url to allow the app to access their Google calendars and get back an authorization code
	 * @throws Exception 
	 */
	private static void askUserToAuthorizeApp() throws Exception {
		System.out.println("");
		System.out.println("Step 1: Getting User Authorization");
		
		authorizationURL = endpoint + "?" 
				+ "scope=" + calScope + "&" 
				+ "redirect_uri=" + redirect_uri + "&" 
				+ "response_type=code&"
				+ "client_id=" + clientID + "&"
				+ "approval_prompt=force";
		
		System.out.println("Visit the url below \nclick allow, copy-paste the string in the url starting at '4/....' \ne.g 4/Cjfa6532DWJzL1PrFEezp0RYdsD9.cmxLylP21JEe3oEBd8DOtNDRyKjfjgI \nHit enter to continue");
		System.out.println("*");
		System.out.println(authorizationURL);
		
		// Connect to the authorization url
		URL url = new URL(authorizationURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setInstanceFollowRedirects(true);
		request.connect();
		
		// Wait for the user input i.e. an authorization code
		Scanner console = new Scanner(System.in);
		String entry = console.nextLine();
		if(!entry.isEmpty()){
			authorization_code = entry;
			requestAnAccessToken();
		} else {
			System.out.println("Oops! something went wrong. Try it again? Y/N");
			Scanner console2 = new Scanner(System.in);
			String redo = console2.nextLine();
			if (redo.equalsIgnoreCase("y")){
				askUserToAuthorizeApp();
			} else {
				System.exit(0);
			}
		}
	}
	
	/**
	 * Request an access token or a refresh token if an authorization code is available
	 * @throws Exception 
	 */
	private static void requestAnAccessToken() throws Exception {
		System.out.println("");
		System.out.println("Step 2: Requesting Access Token");		
		
		String charset = "UTF-8";
		String query = String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=%s",
				URLEncoder.encode(authorization_code, charset),
				URLEncoder.encode(clientID, charset),
				URLEncoder.encode(clientSecret, charset),
				URLEncoder.encode(redirect_uri, charset),
				URLEncoder.encode("authorization_code", charset));
		
		HttpURLConnection connection = (HttpURLConnection) new URL(tokenRequestURL).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Host","accounts.google.com");
		connection.setRequestProperty("Accept-Charset", charset);
		
		//  Create an output stream so that we can later read the response back in
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(query);
			wr.flush();
			wr.close();
		
		// String response = connection.getResponseMessage();
		// Check if POST was successful
		int statCode = connection.getResponseCode();
		// System.out.println("Result : " + Integer.toString(statCode) + " " + response);
		
		if( statCode == 200) {
			// Read in the response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String conInput;
			StringBuilder sb = new StringBuilder();
			while ((conInput = in.readLine()) != null) {
				sb.append(conInput);
			}
			in.close();
			// System.out.println(sb.toString());
			// Parse the string to Json
			JsonParser jparser = new JsonParser();
			rootObjectTokens = jparser.parse(sb.toString()).getAsJsonObject(); 
			access_token = rootObjectTokens.get("access_token").getAsString();
			if ((access_token != null)){
				// Access token found so set check to true so that program can attempt to access Calendar API
				accessCalendars = true;
			} else {
				// No access token found
				System.out.println("Something went wrong. Tokens are null");
			}
		} else {
			System.out.println("An error occured! Error code: " + Integer.toString(statCode));
			System.out.println("Try it again? Y/N");
			Scanner console2 = new Scanner(System.in);
			String redo = console2.nextLine();
			if (redo.equalsIgnoreCase("y")){
				askUserToAuthorizeApp();
			} else {
				System.exit(0);
			}
		} 
		
	}
	
	/**
	 * Attempt to call the CalendarList API to get a list of a user's Google Calendars
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	private static void callCalendarAPI() throws MalformedURLException, IOException, ParseException {
		System.out.println("Step 3: Attempting to access your Google Calendars");
		String url = calBase_uri + calList + "?access_token=" + access_token;
		System.out.println(url);
		
		HttpURLConnection urlconn = (HttpURLConnection) new URL(url).openConnection();
		urlconn.setRequestMethod("GET");
		
		String response = urlconn.getResponseMessage();
		// Check if GET was successful
		int statCode = urlconn.getResponseCode();
		// System.out.println("Result : " + Integer.toString(statCode) + " " + response);
		
		if( statCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
			String conInput;
			StringBuilder sb = new StringBuilder();
			while ((conInput = in.readLine()) != null) {
				sb.append(conInput);
			}
			in.close();
			// Parse the string to Json
			JsonParser jp = new JsonParser();
			rootCalendarList = jp.parse(sb.toString()).getAsJsonObject(); 
			
			printTheCalendarsData(rootCalendarList);
			
		} else {
			System.out.println("Error code : " + statCode);
		}
		
	}
	
	/**
	 * Get the list of all calendars and extract calendar ID's
	 * Print the data for the first calendar
	 * @param list
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	private static void printTheCalendarsData(JsonObject list) throws MalformedURLException, IOException, ParseException {
		System.out.println("Here are your Google Calendars");
		System.out.println(" ");
		JsonArray calendars = list.get("items").getAsJsonArray();
		int i = 0;
		for(i=0; i<calendars.size(); i++){
			JsonElement jelement = calendars.get(i);
			if(i == 0){
				calID = jelement.getAsJsonObject().get("id").getAsString();
			}
			String summary = jelement.getAsJsonObject().get("summary").getAsString();
			System.out.println(summary);
		}
		// Print the first calendar
		printEventsForThisCalendar(calID);
	}
	
	/**
	 * Takes a calendar Id and makes another call to the API to get all the events
	 * for a specific Calendar, then prints the date, time and name of the event
	 * @param cal_id
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	private static void printEventsForThisCalendar(String cal_id) throws MalformedURLException, IOException, ParseException {
		System.out.println("Here are the events the " + cal_id + " calendar");
		System.out.println(" ");
		String url = calBase_uri + calendar + cal_id + "/events?access_token=" + access_token;
		// System.out.println(url);
		HttpURLConnection uconn = (HttpURLConnection) new URL(url).openConnection();
		uconn.setRequestMethod("GET");
		String response = uconn.getResponseMessage();
		// Check if GET was successful
		int statCode = uconn.getResponseCode();
		// System.out.println("Result : " + Integer.toString(statCode) + " " + response);
		
		if( statCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(uconn.getInputStream()));
			String conInput;
			StringBuilder sb = new StringBuilder();
			while ((conInput = in.readLine()) != null) {
				sb.append(conInput);
			}
			in.close();
			
			// Parse the response string to Json
			JsonParser jp = new JsonParser();
			rootCalendarEvents = jp.parse(sb.toString()).getAsJsonObject();
			JsonArray events = rootCalendarEvents.get("items").getAsJsonArray();
			int i = 0;
			for(i = 0; i<events.size(); i++){
				JsonElement jelement = events.get(i);
				String summary = jelement.getAsJsonObject().get("summary").getAsString();
				JsonElement datetime = jelement.getAsJsonObject().get("start").getAsJsonObject().get("dateTime");
				String when;
				java.util.Date edate;
				if(datetime != null){
				// Date is in this format : - 2011-05-19T12:00:00-04:00
					when = datetime.getAsString();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'-04:00'", Locale.ENGLISH);
					edate = df.parse(when);
				} else {
					// Date is in this format : - 2011-05-19
					when = jelement.getAsJsonObject().get("start").getAsJsonObject().get("date").getAsString();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
					edate = df.parse(when);
				}
				
				System.out.println("*");
				System.out.println("Event Title : " + summary);
				System.out.println("On : " + edate);
				System.out.println();
				
			}			
			
		} else {
			System.out.println("Error code : " + statCode);
		}
	}
}
