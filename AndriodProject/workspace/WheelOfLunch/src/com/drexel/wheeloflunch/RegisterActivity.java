package com.drexel.wheeloflunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.callbacks.CreationResponseCallback;
import com.cloudmine.api.rest.response.CreationResponse;
import com.cloudmine.api.rest.response.code.CMResponseCode;

public class RegisterActivity extends Activity 
{
	private static final String APP_ID = "847c4a9387424df28636c133b4b6ec0a";
	private static final String API_KEY = "77e84025f6254ba48544fa87c0a3ebbb";
	
	private EditText usernameIn;
	private EditText passwordIn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
		
		usernameIn = (EditText)findViewById(R.id.usernameSign);
		passwordIn = (EditText)findViewById(R.id.passwordSign);
	}
	
	public void login(View view)
	{	
		final String usernameTxt = usernameIn.getText().toString();
		final String passwordTxt = passwordIn.getText().toString();
		
		final CMUser user = new CMUser(usernameTxt, passwordTxt);
		 
		user.createUser(new CreationResponseCallback() 
		{
		    public void onCompletion(CreationResponse response) 
		    {
		        //System.out.println("was user created? " + response.wasSuccess());
		        boolean userAlreadyExists = response.getResponseCode().equals(CMResponseCode.EMAIL_ALREADY_EXISTS);
		        if(userAlreadyExists) 
		        {
		        	Toast.makeText(getApplicationContext(), "User with that e-mail address already exists; user was not created", Toast.LENGTH_SHORT).show();
		        	//System.out.println("User with that e-mail address already exists; user was not created");
		        } 
		        else 
		        {
		            // configure the store with the user so that it can perform user-centric operations
		            //CMStore.getStore().setUser(user);
		        	Intent i = new Intent(getBaseContext(), LoginActivity.class); 
		        	//LoginActivity la = new LoginActivity();
		        	//EditText et = (EditText)la.findViewById(R.id.usernameIn);
		        	//et.setText(usernameTxt);
		    		startActivity(i);
		        }
		    }
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
