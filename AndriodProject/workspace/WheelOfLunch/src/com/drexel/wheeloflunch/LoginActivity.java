package com.drexel.wheeloflunch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudmine.api.CMAndroidSocial;
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.CMSocial;
import com.cloudmine.api.rest.CMStore;
import com.cloudmine.api.rest.callbacks.CMSocialLoginResponseCallback;
import com.cloudmine.api.rest.callbacks.LoginResponseCallback;
import com.cloudmine.api.rest.response.CMSocialLoginResponse;
import com.cloudmine.api.rest.response.LoginResponse;

public class LoginActivity extends Activity 
{
	private static final String APP_ID = "847c4a9387424df28636c133b4b6ec0a";
	private static final String API_KEY = "77e84025f6254ba48544fa87c0a3ebbb";
	
	private EditText  username=null;
	private EditText  password=null;
	private Button login;
	private Button register;
	int counter = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
		
		username = (EditText)findViewById(R.id.usernameIn);
		password = (EditText)findViewById(R.id.passwordIn);
		login = (Button)findViewById(R.id.signInBtn);
		register = (Button)findViewById(R.id.regBtn);
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGINPREF", MODE_PRIVATE); 
		//pref.edit().clear();
		
		String un = pref.getString("USRNAME", null);
        String ps = pref.getString("PSSWORD", null);
        String sc = pref.getString("SOCIAL", null);
		
        CMAndroidSocial cs;
		if (un == null && ps == null )
		{
			// the key does not exist
		} 
		else 
		{
			if(sc == null)
			{
				// handle the value
				Intent i = new Intent(getBaseContext(), MainActivity.class);                      
	    		startActivity(i);
	    		
	    		final CMUser user = new CMUser(un, ps);
	   		 
	    		user.login(new LoginResponseCallback() 
	    		{
	    		    public void onCompletion(LoginResponse response) 
	    		    {
	    		        if(response.wasSuccess()) 
	    		        {
	    		        	//Toast.makeText(getApplicationContext(), "Redirecting...", 
	    							//Toast.LENGTH_SHORT).show();
	    		            // configure the store with the user so that it can perform user-centric operations
	    		            CMStore.getStore().setUser(user);
	    		            
	    		            Intent i = new Intent(getBaseContext(), MainActivity.class);                      
	    		    		startActivity(i);
	    		        } 
	    		        else 
	    		        {
	    		        	Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
	    		        	System.out.println("We failed to log in because of: " + response.getResponseCode());
	    		        }
	    		    }
	    		 
	    		    public void onFailure(Throwable t, String msg) 
	    		    {
	    		        System.out.println("The call was never made because of: " + msg);
	    		    }
	    		});
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void loginWithFacebook(View view)
	{
		Toast.makeText(getApplicationContext(), "Currenlty broken...:( Sorry", Toast.LENGTH_SHORT).show();
		/*CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());
		
		CMAndroidSocial.loginThroughSocial(CMSocial.Service.FACEBOOK, this, new CMSocialLoginResponseCallback() 
		{
		    public void onCompletion(CMSocialLoginResponse response)
		    {
		        System.out.println("Authenticated to: " + response.getUser().getAuthenticatedServices());
		        System.out.println("Logged in? " + response.getSessionToken().isValid());
		        
		        if(response.wasSuccess())
		        {
		        	SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGINPREF", MODE_PRIVATE); 
		        	Editor editor = pref.edit();
		            
		            editor.putString("SOCIAL", "true");
		            editor.commit();
			        
			        Intent i = new Intent(getBaseContext(), MainActivity.class);                      
		    		startActivity(i);

			        // configure the store with the user so that it can perform user-centric operations
			        CMStore.getStore().setUser(response.getUser());
		        }  
		        else 
		        {
		        	Toast.makeText(getApplicationContext(), "Social Fail", Toast.LENGTH_SHORT).show();
		        	System.out.println("We failed to log in because of: " + response.getResponseCode());
		        }
		        
		    }
		 
		    @Override
		    public void onFailure(Throwable t, String msg) 
		    {
		        System.out.println("Oh no we failed! " + t.getMessage());
		    }
		});*/
	}
	public void login(View view)
	{
		final CMUser user = new CMUser(username.getText().toString(), password.getText().toString());
		 
		user.login(new LoginResponseCallback() 
		{
		    public void onCompletion(LoginResponse response) 
		    {
		        if(response.wasSuccess()) 
		        {
		        	Toast.makeText(getApplicationContext(), "Redirecting...", 
							Toast.LENGTH_SHORT).show();
		            // configure the store with the user so that it can perform user-centric operations
		            CMStore.getStore().setUser(user);
		            
		            SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGINPREF", MODE_PRIVATE); 
		            Editor editor = pref.edit();
		            
		            editor.putString("USRNAME", username.getText().toString());
		            editor.putString("PSSWORD", password.getText().toString());
		            editor.commit();
		            
		            Intent i = new Intent(getBaseContext(), MainActivity.class);                      
		    		startActivity(i);
		        } 
		        else 
		        {
		        	Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
		        	System.out.println("We failed to log in because of: " + response.getResponseCode());
		        }
		    }
		 
		    public void onFailure(Throwable t, String msg) {
		        System.out.println("The call was never made because of: " + msg);
		    }
		});
	}
	public void register(View view)
	{
		Intent i = new Intent(getBaseContext(), RegisterActivity.class);                      
		//i.putExtra("PersonID", personID);
		startActivity(i);
		/*final CMUser user = new CMUser("steve.a.calabro@gmail.com", "test");
		 
		user.c reateUser(new CreationResponseCallback() 
		{
		    public void onCompletion(CreationResponse response) 
		    {
		        System.out.println("was user created? " + response.wasSuccess());
		        boolean userAlreadyExists =
		                          response.getResponseCode().equals(CMResponseCode.EMAIL_ALREADY_EXISTS);
		        if(userAlreadyExists) {
		        	System.out.println("User with that e-mail address already exists; user was not created");
		        } 
		        else 
		        {
		            // configure the store with the user so that it can perform user-centric operations
		            CMStore.getStore().setUser(user);
		        }
		    }
		});*/
	}


}
