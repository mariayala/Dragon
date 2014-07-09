package wunderground;

import com.google.gson.JsonObject;

public class Geolookup {
	private String lURL; // lookup URL
	private String city;
	private String state;
	private JsonObject rootobject;
	
	/*
	private String state;
	private String zip_code;
	private String latitude;
	private String longitude;*/ //Other data parameters that can be used 
	
	public Geolookup(String aURL){
		this.lURL = aURL;
	};
	
	public String getURL(){
		lURL = lURL + "/geolookup/q/autoip.json";
		return lURL;
	}
	
	public void setJSONObject(JsonObject robject){
		this.rootobject = robject;
	}
	
	public String getCity(){
		city = rootobject.get("location").getAsJsonObject().get("city").getAsString();
		return city;
	}
	
	public String getState(){
		state = rootobject.get("location").getAsJsonObject().get("state").getAsString();
		return state;
	}


}
