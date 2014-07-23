package start;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sun.net.www.http.HttpClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleCalendarApp {
	static String client_id = "352389790852-79k5qc29hfldc7bjcse75o9bt6mu0m7b.apps.googleusercontent.com";
	static String redirect_uri = "https://www.example.com/oauth2callback";
	static String client_secret = "T1TkK-L6y83d1RzUuhtLLxzZ";
	
	// OAuth 2.0 Google Calendar API scopes
	static String manage_calendar = "https://www.googleapis.com/auth/calendar";
	static String view_calendar = "https://www.googleapis.com/auth/calendar.readonly";
	
	static String authorization_code = "code=4/njVJ-Kw1axEQcpukASN0efPOxr3k.EmObvMsjBaUUaDn_6y0ZQNhqa3S_jgI";
	
	public static void main(String[] args) throws Exception{
		
		
		String baseURL = "https://www.googleapis.com/calendar/v3";
		String calendarListURL = "https://www.googleapis.com/calendar/v3/users/me/calendarList?";
		String iURLcalendar = "https://www.googleapis.com/calendar/v3/calendars/";
		String calendarID;
		JsonObject rootObject;
		String key = "";
		
		// rootObject = connectToURL(calendarListURL);
		// System.out.println(rootObject);
		
		authenticateTheUser();
		getAccessTokens();
	}

	private static void authenticateTheUser() throws Exception {
		String aURL = "https://accounts.google.com/o/oauth2/auth?"
				+ "scope="+view_calendar+"&"
				+ "redirect_uri=" + redirect_uri + "&"
				+ "response_type=code&"
				+ "client_id=" + client_id + "&"
				+ "approval_prompt=force";
		System.out.println(aURL);
		URL url = new URL(aURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		
		// Get the response
		int code = request.getResponseCode();
		System.out.println("Response code is " + Integer.toString(code));
		String responseMsg = request.getResponseMessage();
		System.out.println("Response msg is " + responseMsg);			
	}
	
	private static void getAccessTokens() throws Exception {
		String aURL = "https://accounts.google.com/o/oauth2/auth?"
				+ authorization_code + "&"
				+ "client_id=" + client_id + "&"
				+ "client_secret=" + client_secret + "&"
				+ "redirect_uri=" + redirect_uri + "&"
				+ "grant_type=authorization_code";
		
		URL url = new URL(aURL);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.addRequestProperty("POST","/o/oauth2/token HTTP/1.1");
		request.addRequestProperty("Host","accounts.google.com");
		request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		request.connect();
		
		System.out.println("*Token code from this url");
		System.out.println(aURL);
		int code = request.getResponseCode();
		System.out.println("Access token code " + Integer.toString(code));
		/*
		POST /o/oauth2/token HTTP/1.1
		Host: accounts.google.com
		Content-Type: application/x-www-form-urlencoded
		*/
	}

}
