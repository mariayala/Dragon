Name: Maria A Yala
Java Project - Wunderground

Wunderground is a 3 class Java project that has
the following classes:
1. Geolookup.java
	This class creates an object that contains the user's
geographic location that can be used to get user city & state or more.

2. HourlyForecast.java
	This class prints the houly forecast for a user and tells them the weather info for a particular time, date, and place it lists the temperature, humidity and current conditions.

3. WeatherApp.java
	This class contains the main function and prompts the user to enter a key in this case "1b096d97a830f4bf" then creates a Geolookup object calls the URL and then gets the user's city and state. These parameters are used to form the URL for the hourly forecast, a new HourlyForecast is made and a HTTP connection is made. The hourly forecast is then printed by a call to printForecast(); 
