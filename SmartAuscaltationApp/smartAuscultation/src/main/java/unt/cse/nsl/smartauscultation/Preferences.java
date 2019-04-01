package unt.cse.nsl.smartauscultation;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity
{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		addPreferencesFromResource( R.xml.preferences );
	}
	
	@Override
	public void onConfigurationChanged( Configuration newConfig )
	{
		super.onConfigurationChanged( newConfig );
		setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
	}
}
