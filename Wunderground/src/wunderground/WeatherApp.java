package wunderground;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String key; // authentication key i.e 1b096d97a830f4bf
		String iURL; // URL - no data params
		String lURL; // Complete URL
		String city, state;
		JsonObject rootobj;
		
		if(args.length < 1){
			System.out.println("Enter your user key: ");
			Scanner in = new Scanner(System.in);
			key = in.nextLine();
			in.close();
		} else {
			key = args[0];
		}
		
		iURL = "http://api.wunderground.com/api/" + key;
		
		// ------------  Perform a Geolookup
		// build complete lookup URL
		Geolookup geo = new Geolookup(iURL);
		
		// Connect to the URL & return JSONObject with geolocation data
		lURL = geo.getURL();
		rootobj = connectToUrl(lURL);
		geo.setJSONObject(rootobj);
		
		// Print user's city
		city = geo.getCity();
		
		// ------------  Get hourly forecast
		// get state
		state = geo.getState();
		
		System.out.println();
		System.out.println("* Hourly forecast for " + city + ", " + state + " *");
		
		// put together url params
		String params = state + "/" + city + ".json";
		
		// build complete lookup URL
		HourlyForecast hourly = new HourlyForecast(iURL, params);
		
		// Connect to the URL & return JSONObject with hourly forecast data
		lURL = hourly.getURL();
		rootobj = connectToUrl(lURL);
		hourly.setJSONObject(rootobj);
		
		// print the forecast
		hourly.printForecast();
		
	}
	
	private static JsonObject connectToUrl(String u) throws Exception{
		URL url = new URL(u);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		
		// Convert to a JSON object to print data
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		JsonObject robject = root.getAsJsonObject();
		return robject;
	}
	
}
