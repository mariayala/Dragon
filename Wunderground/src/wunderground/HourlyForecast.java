package wunderground;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HourlyForecast {
	private String sURL;
	private JsonObject rootobject;
	private JsonArray hforecast; 

	public HourlyForecast(String lURL, String params){
		this.sURL = lURL + "/hourly/q/" + params;
	}
	
	public String getURL(){
		return sURL;
	}
	
	public void setJSONObject(JsonObject robject){
		this.rootobject = robject;
	}
	
	public void printForecast(){
		hforecast = rootobject.get("hourly_forecast").getAsJsonArray();
		int size = hforecast.size();
		int i;
		for (i = 0; i <= size-1; i++){
			String dtime = hforecast.get(i).getAsJsonObject().get("FCTTIME").getAsJsonObject().get("pretty").getAsString();
			String dayname = hforecast.get(i).getAsJsonObject().get("FCTTIME").getAsJsonObject().get("weekday_name").getAsString();
			
			System.out.println();
			System.out.println( dayname + " at " + dtime);
			
			String tempC = hforecast.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("english").getAsString();
			String tempF = hforecast.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("metric").getAsString();
			
			System.out.println("Temp: " + tempC + " ºC, " + tempF + " ºF ");
			
			String humidity = hforecast.get(i).getAsJsonObject().get("humidity").getAsString();
			System.out.println("Humidity: " + humidity);
			
			String conditions = hforecast.get(i).getAsJsonObject().get("condition").getAsString();
			System.out.println("Looks like: " + conditions);
			System.out.println();
			
		}
	}

}
