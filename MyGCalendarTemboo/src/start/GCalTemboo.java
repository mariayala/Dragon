package start;

import com.temboo.Library.Google.Calendar.GetAllCalendars;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsInputSet;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsResultSet;
import com.temboo.Library.Google.OAuth.FinalizeOAuth;
import com.temboo.Library.Google.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Google.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Google.OAuth.InitializeOAuth;
import com.temboo.Library.Google.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Google.OAuth.InitializeOAuth.InitializeOAuthResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class GCalTemboo {
	static String access_token = "ya29.SgAghH3FuOZIjyAAAABlxG_hkXG4w_R6bToB3qhdWVgyIqmrJxRHN94UkNWbPA";
	static String refresh_token = "1/nrRU4P0iUXlRYasbuMzkuG6AcUpMAaBxUSzI4R4AZNM";

	public static void main(String[] args) throws TembooException {
		handleOauth();
		// getAllCalendars();
	}

	private static void handleOauth() throws TembooException {
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		TembooSession session = new TembooSession("madey", "myFirstApp", "124e5a9a65234be39f8e0b5ef4805be1");

		InitializeOAuth initializeOAuthChoreo = new InitializeOAuth(session);

		// Get an InputSet object for the choreo
		InitializeOAuthInputSet initializeOAuthInputs = initializeOAuthChoreo.newInputSet();

		// Set inputs
		initializeOAuthInputs.set_ClientID("187642164875-lgvg29k8ou1nrvuvnst7dbf1jff7u5di.apps.googleusercontent.com");
		initializeOAuthInputs.set_Scope("https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.readonly");

		// Execute Choreo
		InitializeOAuthResultSet initializeOAuthResults = initializeOAuthChoreo.execute(initializeOAuthInputs);
		System.out.println(initializeOAuthResults);
		
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		// TembooSession session = new TembooSession("madey", "myFirstApp", "124e5a9a65234be39f8e0b5ef4805be1");

		FinalizeOAuth finalizeOAuthChoreo = new FinalizeOAuth(session);

		// Get an InputSet object for the choreo
		FinalizeOAuthInputSet finalizeOAuthInputs = finalizeOAuthChoreo.newInputSet();

		// Set inputs
		finalizeOAuthInputs.set_CallbackID("madey/d6fcfd06-c1de-4aef-82ca-81cf6e90c85b");
		finalizeOAuthInputs.set_ClientSecret("Dj5LuSFK7jhrHTH3_agfa_vT");
		finalizeOAuthInputs.set_ClientID("187642164875-lgvg29k8ou1nrvuvnst7dbf1jff7u5di.apps.googleusercontent.com");

		// Execute Choreo
		FinalizeOAuthResultSet finalizeOAuthResults = finalizeOAuthChoreo.execute(finalizeOAuthInputs);
		System.out.println(finalizeOAuthResults);
	}

	private static void getAllCalendars() throws TembooException {
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		TembooSession session = new TembooSession("madey", "myFirstApp", "124e5a9a65234be39f8e0b5ef4805be1");

		GetAllCalendars getAllCalendarsChoreo = new GetAllCalendars(session);

		// Get an InputSet object for the choreo
		GetAllCalendarsInputSet getAllCalendarsInputs = getAllCalendarsChoreo.newInputSet();

		// Set inputs
		getAllCalendarsInputs.set_ClientSecret("Dj5LuSFK7jhrHTH3_agfa_vT");
		getAllCalendarsInputs.set_RefreshToken("1/nrRU4P0iUXlRYasbuMzkuG6AcUpMAaBxUSzI4R4AZNM");
		getAllCalendarsInputs.set_ClientID("187642164875-lgvg29k8ou1nrvuvnst7dbf1jff7u5di.apps.googleusercontent.com");

		// Execute Choreo
		GetAllCalendarsResultSet getAllCalendarsResults = getAllCalendarsChoreo.execute(getAllCalendarsInputs);
		System.out.println(getAllCalendarsResults);
	}
	
}
