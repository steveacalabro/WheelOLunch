package com.drexel.wheeloflunch;

public class Place 
{
	private String name;
	private int rating;
	private String type;
	private double lat;
	private double lng;
	private int distance;
	
	public Place(String _name, int _rating, String _type, double _lat, double _lng)
	{
		name = _name;
		rating = _rating;
		type = _type;
		lat = _lat;
		lng = _lng;
	}
	
	//getters
	public String getName()
	{
		return name;
	}
	public int getRating()
	{
		return rating;
	}
	public String getType()
	{
		return type;
	}
	public double getLat()
	{
		return lat;
	}
	public double getLong()
	{
		return lng;
	}
	public int getDistance()
	{
		return distance;
	}
	
	//setters
	public void setName(String _name)
	{
		name = _name;
	}
	public void setRating(int _rating)
	{
		rating = _rating;
	}
	public void setType(String _type)
	{
		type = _type;
	}
	public void setLat(double _lat)
	{
		lat = _lat;
	}
	public void setLong(double _lng)
	{
		lng = _lng;
	}
	public void setDistance(int _distance)
	{
		distance = _distance;
	}
}
