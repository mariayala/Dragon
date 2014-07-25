package start;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.temboo.Library.Google.Calendar.GetAllCalendars;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsInputSet;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsResultSet;
import com.temboo.Library.Google.Calendar.GetAllEvents;
import com.temboo.Library.Google.Calendar.GetAllEvents.GetAllEventsInputSet;
import com.temboo.Library.Google.Calendar.GetAllEvents.GetAllEventsResultSet;
import com.temboo.Library.Google.OAuth.FinalizeOAuth;
import com.temboo.Library.Google.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Google.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Google.OAuth.InitializeOAuth;
import com.temboo.Library.Google.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Google.OAuth.InitializeOAuth.InitializeOAuthResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class GCalTemboo {
	static String access_token = "";
	static String refresh_token = "";
	
	static String authorizationURL = "";
	static String callbackID = "";
	
	static String clientSecret = "Dj5LuSFK7jhrHTH3_agfa_vT";
	static String clientID = "187642164875-lgvg29k8ou1nrvuvnst7dbf1jff7u5di.apps.googleusercontent.com";
	
	static JsonObject rootObject;
	
	// Calendars
	static String calScope = "https://www.googleapis.com/auth/calendar.readonly";
	static String calID = "";
	
	public static void main(String[] args) throws TembooException, ParseException {
		handleOauth();
		getAllCalendars();
		printTheCalendars();
	}

	private static void handleOauth() throws TembooException {
		/* InitializeOAuth */
		
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		TembooSession session = new TembooSession("madey", "myFirstApp", "124e5a9a65234be39f8e0b5ef4805be1");

		InitializeOAuth initializeOAuthChoreo = new InitializeOAuth(session);
		
		// Get an InputSet object for the choreo
		InitializeOAuthInputSet initializeOAuthInputs = initializeOAuthChoreo.newInputSet();

		// Set inputs
		initializeOAuthInputs.set_ForwardingURL("https://www.cs.drexel.edu/~may36/");
		initializeOAuthInputs.set_ClientID(clientID);
		initializeOAuthInputs.set_Scope(calScope);

		// Execute Choreo
		InitializeOAuthResultSet initializeOAuthResults = initializeOAuthChoreo.execute(initializeOAuthInputs);
		authorizationURL = initializeOAuthResults.get_AuthorizationURL();
		callbackID = initializeOAuthResults.get_CallbackID();
		
		System.out.println("Initialize OAuth");
		System.out.println("Go to the url below and click allow:");
		System.out.println(authorizationURL);
		
		Scanner console = new Scanner(System.in);
		String entry = console.nextLine(); // Wait for user to hit "enter" before continuing
		console.close();
		
		/* FinalizeOAuth */
		FinalizeOAuth finalizeOAuthChoreo = new FinalizeOAuth(session);

		// Get an InputSet object for the choreo
		FinalizeOAuthInputSet finalizeOAuthInputs = finalizeOAuthChoreo.newInputSet();

		// Set inputs
		finalizeOAuthInputs.set_CallbackID(callbackID);
		finalizeOAuthInputs.set_ClientSecret(clientSecret);
		finalizeOAuthInputs.set_Timeout("60");
		finalizeOAuthInputs.set_ClientID(clientID);

		// Execute Choreo
		FinalizeOAuthResultSet finalizeOAuthResults = finalizeOAuthChoreo.execute(finalizeOAuthInputs);
		
		System.out.println("Finalize OAuth");
		access_token = finalizeOAuthResults.get_AccessToken();
		refresh_token = finalizeOAuthResults.get_RefreshToken();
		System.out.println("Access token expires after : " + finalizeOAuthResults.get_Expires());
		System.out.println(" ");
	}

	private static void getAllCalendars() throws TembooException {
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		TembooSession session = new TembooSession("madey", "myFirstApp", "124e5a9a65234be39f8e0b5ef4805be1");

		GetAllCalendars getAllCalendarsChoreo = new GetAllCalendars(session);

		// Get an InputSet object for the choreo
		GetAllCalendarsInputSet getAllCalendarsInputs = getAllCalendarsChoreo.newInputSet();

		// Set inputs
		getAllCalendarsInputs.set_ClientSecret(clientSecret);
		getAllCalendarsInputs.set_RefreshToken(refresh_token);
		getAllCalendarsInputs.set_ClientID(clientID);

		// Execute Choreo
		GetAllCalendarsResultSet getAllCalendarsResults = getAllCalendarsChoreo.execute(getAllCalendarsInputs);
		System.out.println("Here are your Google Calendars");
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(getAllCalendarsResults.get_Response());
		rootObject = root.getAsJsonObject();
		
		System.out.println("");
	}
	
	private static void printTheCalendars() throws TembooException, ParseException {
		JsonArray calendars = rootObject.get("items").getAsJsonArray();
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

	private static void printEventsForThisCalendar(String cal_id) throws TembooException, ParseException {
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		TembooSession session = new TembooSession("madey", "myFirstApp", "124e5a9a65234be39f8e0b5ef4805be1");

		GetAllEvents getAllEventsChoreo = new GetAllEvents(session);

		// Get an InputSet object for the choreo
		GetAllEventsInputSet getAllEventsInputs = getAllEventsChoreo.newInputSet();

		// Set inputs
		getAllEventsInputs.set_ClientSecret(clientSecret);
		getAllEventsInputs.set_CalendarID(calID);
		getAllEventsInputs.set_RefreshToken(refresh_token);
		getAllEventsInputs.set_ClientID(clientID);

		// Execute Choreo
		GetAllEventsResultSet getAllEventsResults = getAllEventsChoreo.execute(getAllEventsInputs);
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(getAllEventsResults.get_Response());
		rootObject = root.getAsJsonObject();
		
		JsonArray events = rootObject.get("items").getAsJsonArray();
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
	}
}
