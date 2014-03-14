package com.drexel.wheeloflunch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.SimpleCMObject;
import com.cloudmine.api.rest.callbacks.ObjectModificationResponseCallback;
import com.cloudmine.api.rest.response.ObjectModificationResponse;

public class NewPlaceActivity extends Activity 
{
	private EditText nameTxt = null;
	private EditText ratingTxt = null;
	private EditText typeTxt = null;
	private EditText latTxt = null;
	private EditText longTxt = null;
	
	private static final String APP_ID = "847c4a9387424df28636c133b4b6ec0a";
	private static final String API_KEY = "77e84025f6254ba48544fa87c0a3ebbb";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newplace);
	    
		// initialize CloudMine library
	    CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
	    
	    nameTxt = (EditText)findViewById(R.id.nameTxt);
		ratingTxt = (EditText)findViewById(R.id.rateTxt);
		typeTxt = (EditText)findViewById(R.id.typeTxt);
		latTxt = (EditText)findViewById(R.id.latTxt);
		longTxt = (EditText)findViewById(R.id.longTxt);
	}
	public void newPlace(View view)
	{	
		// create a honda civic
	    SimpleCMObject place = new SimpleCMObject();
	    
	    
	    SimpleCMObject o = new SimpleCMObject();
	    place.add("name", nameTxt.getText().toString());
	    place.add("rating", Integer.parseInt(ratingTxt.getText().toString()));
	    place.add("type", typeTxt.getText().toString());
	    place.add("lat", Double.parseDouble(latTxt.getText().toString()));
	    place.add("long", Double.parseDouble(longTxt.getText().toString()));
	    final String objectId = o.getObjectId();  // object id automatically generated
	     
	    o.save(new ObjectModificationResponseCallback() 
	    {
	        public void onCompletion(ObjectModificationResponse response) 
	        {
	            System.out.println("Response was a success? " + response.wasSuccess());
	            System.out.println("We: " + response.getKeyResponse(objectId) + " the simple object");
	        }
	        public void onFailure(Throwable e, String msg) 
	        {
	        	System.out.println("We failed: " + e.getMessage());
	        }
	    });
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.newplace, menu);
		return true;
	}

}
