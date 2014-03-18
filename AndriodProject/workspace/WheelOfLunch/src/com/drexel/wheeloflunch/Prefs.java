package com.drexel.wheeloflunch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class Prefs extends PreferenceActivity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.prefs);
        
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DISTANCEPREF", MODE_PRIVATE);
        
        String closePref = pref.getString("close", null);
		String medPref = pref.getString("med", null);
		String farPref = pref.getString("far", null);
		
        if(closePref != null)
		{
        	ListPreference close = (ListPreference) findPreference("pref_close");
            close.setDefaultValue(closePref);
		}
		if(medPref != null)
		{
			ListPreference med = (ListPreference) findPreference("pref_med");
	        med.setDefaultValue(medPref);
		}
		if(farPref != null)
		{
			ListPreference far = (ListPreference) findPreference("pref_far");
	        far.setDefaultValue(farPref);
		}
    } 

    @Override
    public void onPause()
    {
    	super.onPause();
    	ListPreference close = (ListPreference) findPreference("pref_close");
        String closeVal = close.getValue();
        
        ListPreference med = (ListPreference) findPreference("pref_med");
        String medVal = med.getValue();
        
        ListPreference far = (ListPreference) findPreference("pref_far");
        String farVal = far.getValue();
        
    	SharedPreferences pref = getApplicationContext().getSharedPreferences("DISTANCEPREF", MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();
        editor.putString("close", closeVal);
        editor.putString("med", medVal);
        editor.putString("far", farVal);
        editor.commit(); 
    }
}
