public class DistanceAlg
{
	private double lat1;
	private double lon1;
	private double lat2;
	private double lon2;
	private double close = .5;
	private double med = 1;
	private double far = 5;

	public DistanceAlg(double lat1, double lon1, double lat2, double lon2)
	{
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lat2 = lat2;
		this.lon2 = lon2;
	}

	public int getDistance()	
	{
		double earthRadius = 3958.756;
		double dLat = Math.toRadians(this.lat2-this.lat1);
		double dLon = Math.toRadians(this.lon2-this.lon1);
		double lat1 = Math.toRadians(this.lat1);
		double lat2 = Math.toRadians(this.lat2);

		double a = Math.pow(Math.sin(dLat/2),2) + Math.pow(Math.sin(dLon/2),2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double distance = earthRadius * c;
		return getRank(distance);
	}

	public int getRank(double distance)
	{
		if (distance <= close) return 1;
		else if (distance <= med) return 2;
		else if (distance <= far) return 3;
		else return 0;
	}
	
	public void setDistancePref(String distLabel, double distance)
	{
		if (distLabel.equals("close")) close = distance;
		else if (distLabel.equals("med")) med = distance;
		else if (distLabel.equals("far")) far = distance;
		else System.out.println("Invalid label");
	}
}
