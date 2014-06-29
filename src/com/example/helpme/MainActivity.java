package com.example.helpme;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

	

@SuppressLint("NewApi")
public class MainActivity extends Activity  {
	
	GPStracker gps;
	String Sparrownumber="5455";
	boolean shake=false;
	int force,duration;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Read the preference file
        SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
        shake=settings.getBoolean("check", true);
        force=settings.getInt("force", 1);
        duration=settings.getInt("duration", 1);
        
        if(!displayGpsStatus())
        {
        	turnGPSOn();
        }
        
        
        Button b = (Button) findViewById(R.id.button1);
        
        b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.v("SMS","Clicked");
			
				// TODO Auto-generated method stub
				gps = new GPStracker(MainActivity.this);
				 
                // check if GPS enabled     
                if(gps.canGetLocation()){
                     
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                 
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                
                String message=getResources().getString(R.string.messagetosend);
                String cat="Emergency";
                String type="present";
                
                String code=getJSON(type+","+cat+","+message,MainActivity.this);
                
                sendSMSSparrow("apikabach "+code,Sparrownumber);
                sendSMS(code);
                Log.v("SMS","SENT");
                Log.v("SMS","SENT TO SPARROW");
			}
        	
        });
        
        
    }
    
    private Boolean displayGpsStatus() {  
    	  ContentResolver contentResolver = getBaseContext()  
    	  .getContentResolver();  
    	  boolean gpsStatus = Settings.Secure  
    	  .isLocationProviderEnabled(contentResolver,   
    	  LocationManager.GPS_PROVIDER);  
    	  if (gpsStatus) {  
    	   return true;  
    	  
    	  } else {  
    	   return false;  
    	  }  
    	 } 
    
    private void turnGPSOn() {

        String provider = android.provider.Settings.Secure.getString(
                getContentResolver(),
                android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { // if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
    
    
    @Override
    protected void onResume() {
      super.onResume();
      SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
      shake=settings.getBoolean("check", true);
      force=settings.getInt("force", 1);
      duration=settings.getInt("duration", 1);
      
      if(shake)
      stopService(new Intent(this, ShakeDectector.class));
    }
    
    public String getJSON(String test,Context c)
	{
		String[] array=test.split(",");
		
		GPStracker gps = new GPStracker(c);
		double latitude=-1;
		double longitude=-1;
        // check if GPS enabled     
        if(gps.canGetLocation()){
             
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();    
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        
        String code= "["+"["+Double.toString(latitude)+","+Double.toString(longitude)+
        "],"+array[0]+","+array[1]+","+array[2]+"]";
        
        //Log.d("Bidhya",code);
        
		
		return code;
	}

    @Override
    protected void onPause() {
      //mSensorManager.unregisterListener(mSensorListener);
    	SharedPreferences settings=PreferenceManager.getDefaultSharedPreferences(this);
        shake=settings.getBoolean("check", true);
        force=settings.getInt("force", 1);
        duration=settings.getInt("duration", 1);
        if(shake)
        {
        	Intent i=new Intent(this, ShakeDectector.class);
        	i.putExtra("force", force);
        	i.putExtra("duration", duration);
        	startService(i);
        }
      super.onStop();
      
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    

//Method to handle the on-click operation of the menu bar items
@Override
public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
    case R.id.setting:    
        showSelectList(this.getApplicationContext());
        return true;
    case R.id.report:
    	showReportDialog(this.getApplicationContext());
        return true;
    default:
        return super.onOptionsItemSelected(item);
}

}



//AlertBox that shows the report input box

public void showReportDialog(Context c) {
    DialogFragment newFragment = new Report();
    newFragment.show(getFragmentManager(), "report");
}



//Main navigator to show to chose either set message or set contact numbers
public void showSelectList(Context c) {
    DialogFragment newFragment = new Select(c);
    newFragment.show(getFragmentManager(), "select");
}

public void sendSMS(String message) {
	
	String[]phone = getResources().getStringArray(R.array.contact_array);
   
    //String message = getResources().getString(R.string.messagetosend);
    
    

    SmsManager smsManager = SmsManager.getDefault();
    for(int i=0;i<phone.length;i++){
    smsManager.sendTextMessage(phone[i], null, message, null, null);
    }
    Log.d(null, "message sent");
}

public void sendSMSSparrow(String message,String number) {
    SmsManager smsManager = SmsManager.getDefault();
    
    smsManager.sendTextMessage(number, null, message, null, null);
    
    Log.d("Bidhya", "message sent");
}

	
	
}
    
    
  
