package com.drexel.wheeloflunch;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.SimpleCMObject;
import com.cloudmine.api.rest.CMStore;
import com.cloudmine.api.rest.callbacks.CMObjectResponseCallback;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements LocationListener
{
	private static final String APP_ID = "847c4a9387424df28636c133b4b6ec0a";
	private static final String API_KEY = "77e84025f6254ba48544fa87c0a3ebbb";
	
	// Google Map
    private GoogleMap googleMap;
    //static final LatLng TutorialsPoint = new LatLng(21 , 57);
    
    private LocationManager locationManager;
    private String provider;
    
    public double latitude;
    public double longtitude;
    
    public int weather;
    
    public ArrayList<Place> placeList = new ArrayList<Place>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
		
		//maps
		try 
		{ 
			if (googleMap == null) 
			{
				googleMap = ((MapFragment) getFragmentManager().
						findFragmentById(R.id.map)).getMap();
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			/*Marker TP = googleMap.addMarker(new MarkerOptions().
					position(TutorialsPoint).title("TutorialsPoint"));*/

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		//get location
		//txtLat = (TextView) findViewById(R.id.textview1);
		 
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if enabled and if not send user to the GPS settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) 
		{
	        buildAlertMessageNoGps();
	    }
		else
		{
			Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
		}
		
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    
		// default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);

	    // Initialize the location fields
	    if (location != null) 
	    {
	      //System.out.println("Provider " + provider + " has been selected.");
	      onLocationChanged(location);
	    } 
	    else 
	    {
	    	///enter zipcode
	    	//latituteField.setText("Location not available");
	    	//longitudeField.setText("Location not available");
	    }
	    
		//run through weather
	    WeatherAlg wa = new WeatherAlg();
	    try 
	    {
	    	TextView line1 = (TextView)findViewById(R.id.wa1);
	    	TextView line2_1 = (TextView)findViewById(R.id.wa2);
	    	TextView line2_2 = (TextView)findViewById(R.id.wa3);
			
	    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	        
	    	wa.getWeather();
	    	weather = wa.getValue();
	    	line1.setText(" "+wa.getForecast());
	    	line2_1.setText("Temp: "+wa.getTemperature()+"F    ");
	    	line2_2.setText("Feels Like: "+wa.getFeelsLike()+"F");
	    } 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    	Toast.makeText(getApplicationContext(), "Can't set weather", Toast.LENGTH_SHORT).show();
	    	System.out.println("Can't set weather");
	    }
	    
	    fillPlaces();
	    
	}
	
	public void spinPlaces(View view)
	{
		if(placeList.size() > 0)
		{
			Random generator = new Random();
			int num = generator.nextInt(placeList.size());
			
			TextView tv = (TextView)findViewById(R.id.spinChoice);
			
			Place p = placeList.get(num);
			tv.setText(p.getName());
			
			// create marker
			MarkerOptions marker = new MarkerOptions().position(new LatLng(p.getLat(), p.getLong())).title(p.getName());
			 
			// adding marker
			googleMap.clear();
			googleMap.addMarker(marker);
			LatLng ll = new LatLng(p.getLat(), p.getLong());
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 17));
		}
		else
		{
				Toast.makeText(getApplicationContext(), "No Places In your area!!!!(Try Reopening)", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void fillPlaces()
	{
		//get the objects
	    CMStore store = CMStore.getStore();
	    
	    store.loadAllApplicationObjects(new CMObjectResponseCallback() 
	    {
	    	public void onCompletion(CMObjectResponse response) 
	    	{	
	    		for(CMObject object : response.getObjects()) 
	    		{
	    			//System.out.println(object);
	    			SimpleCMObject o = new SimpleCMObject(object);
	    			Place p = new Place(o.getString("name"), o.getInteger("rating"), o.getString("type"), o.getDouble("lat"), o.getDouble("long"));
	    			placeList.add(p);
	    			//System.out.println("Added place:"+placeList.size());
	    		}
	    	    
	    	    //run through distance
	    	    if(placeList.size() > 0)
	    	    {
	    	    	for(int i = 0; i < placeList.size(); i++)
	    	    	{
	    	    		Place cur = placeList.get(i);
	    	    		DistanceAlg da = new DistanceAlg(latitude, longtitude, cur.getLat(), cur.getLong());
	    	    		cur.setDistance(da.getDistance());

	    	    		placeList.set(i, cur);
	    	    		//System.out.println("Added place2");
	    	    	}
	    	    	
	    	    	ArrayList<Integer> removeList = new ArrayList<Integer>();
	    	    	
	    	    	//remove far distance/calculate for weather
	    	    	for(int i = 0; i < placeList.size(); i++)
	    	    	{
	    	    		//Distance: 0:>5mile 1:0-.5, 2:.6-1, 3:1-5
	    	    		//Weather: 0-perfect, 1-ehh , 2-bad
	    	    		if(placeList.get(i).getDistance() == 0)
	    	    		{
	    	    			removeList.add(i);
	    	    		}
	    	    		if(weather == 0)
	    	    		{
	    	    			//do nothing its nice out
	    	    		}
	    	    		if(weather == 1)
	    	    		{
	    	    			//weather its ehh
	    	    			if(placeList.get(i).getDistance() == 3)
		    	    		{
	    	    				removeList.add(i);
		    	    		}
	    	    		}
	    	    		if(weather >= 2)
	    	    		{
	    	    			//weather its bad
	    	    			if(placeList.get(i).getDistance() > 1)
		    	    		{
	    	    				removeList.add(i);
		    	    		}
	    	    			
	    	    		}
	    	    	}
	    	    	
	    	    	//remove the bad places
	    	    	for(int i = 0; i < removeList.size(); i++)
	    	    	{
	    	    		int badPlace = removeList.get(i);
	    	    		placeList.remove(badPlace - i);
	    	    	}
	    	    }
	    	    else
	    	    {
	    	    	Toast.makeText(getApplicationContext(), "No Places!(Check internet)", Toast.LENGTH_SHORT).show();
	    	    }
	    	}
	    });
	}
	@Override
	public void onLocationChanged(Location location) 
	{
		latitude = (double) (location.getLatitude());
		longtitude = (double) (location.getLongitude());
		//System.out.println("lat:"+latitude);
		//System.out.println("long:"+longtitude);
		//latituteField.setText(String.valueOf(lat));
		//longitudeField.setText(String.valueOf(lng));
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void onProviderEnabled(String provider) 
	{
		Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();

	}
	@Override
	public void onProviderDisabled(String provider) 
	{
		Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
	}
    /* Request updates at startup */
    @Override
    protected void onResume() 
    {
    	super.onResume();
    	locationManager.requestLocationUpdates(provider, 400, 1, this);
    	fillPlaces();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() 
    {
    	super.onPause();
    	locationManager.removeUpdates(this);
    }
    
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void buildAlertMessageNoGps() 
	{
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
	           {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) 
	               {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() 
	           {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) 
	               {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
	
}
