package com.drexel.wheeloflunch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class WeatherAlg 
{
	double temperature;
	double feelslike;
	double humidity;
	String forecast;
	int value;

	/*public static void main(String[] args) throws Exception {

		WeatherAlg http = new WeatherAlg();
		http.getWeather();

	}*/

	// get request
	public void getWeather() throws Exception 
	{

		String url = "http://freegeoip.net/json";

		// Create connection 
		URL obj = new URL(url);
		HttpURLConnection geoLocation = (HttpURLConnection) obj.openConnection();

		// get response
		geoLocation.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(geoLocation.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// get ZIP code
		//String zip = String.valueOf(parse.getDouble("zipcode"));


		// Make service request to weather underground
		String weatherURL = "http://api.wunderground.com/api/59a82c993f52d68d/conditions/q/19104.xml";

		// Create connection 
		URL weather = new URL(weatherURL);
		HttpURLConnection weatherConnection = (HttpURLConnection) weather.openConnection();

		// get response
		weatherConnection.setRequestMethod("GET");

		//System.out.println("\n\nWeather Forecast\n");
		BufferedReader in2 = new BufferedReader(
				new InputStreamReader(weatherConnection.getInputStream()));
		String inputLine2;
		StringBuffer response2 = new StringBuffer();

		while ((inputLine2 = in2.readLine()) != null) 
		{
			response2.append(inputLine2);
		}
		in2.close();

		// Get Forecast
		String parse = response2.toString();
		//System.out.println(parse);

		//Get weather conditions
		int index = parse.indexOf("<weather>") + 9; 
		String conditions = parse.substring(index,parse.indexOf("<", index));
		//System.out.println(conditions);
		this.forecast = conditions;

		// Get temp
		index = parse.indexOf("<temp_f>") + 8;
		String temp = parse.substring(index,parse.indexOf("<", index));
		double tempVal = Double.valueOf(temp);
		//System.out.println(temp);
		this.temperature = tempVal;

		// Get wind
		index = parse.indexOf("<wind_mph>") + 10;
		String wind = parse.substring(index,parse.indexOf("<", index));
		double windVal = Double.valueOf(wind);
		//System.out.println(wind);

		// Get Humidity
		//index = parse.indexOf("<relative_humidity>") + 19;
		//String humidity = parse.substring(index,parse.indexOf("<", index));
		//double humidityVal = Double.valueOf(humidity);
		//System.out.println(humidity);
		//this.humidity = humidityVal;

		// Get windchill
		index = parse.indexOf("<feelslike_f>") + 13;
		String windchill = parse.substring(index,parse.indexOf("<", index));
		double windChillVal = Double.valueOf(windchill);
		//System.out.println(windchill);
		this.feelslike = windChillVal;

		// Calculate value based on above conditions
		int value = 0;
		String goodBad;

		// if cold, add to value
		if (windChillVal<40){value +=1;}			
		if (windChillVal<20){value +=1;}

		// if hot, add to value
		if (windChillVal>80){value +=1;}			
		if (windChillVal>90){value +=1;}

		// if snow or rain, add to value
		if (conditions.contains("Snow") == true){value +=1;}
		if (conditions.contains("Rain") == true){value +=1;}
		if (conditions.contains("snow") == true){value +=1;}
		if (conditions.contains("rain") == true){value +=1;}

		// print final opinion
		//System.out.println(value);
		this.value = value;

	}

	public double getTemperature(){return this.temperature;}
	//public double getHumidity(){return this.humidity;}
	public double getFeelsLike(){return this.feelslike;}
	public String getForecast(){return this.forecast;}
	public int getValue(){return this.value;}

}
