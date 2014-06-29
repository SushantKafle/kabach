package com.example.helpme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class ShakeDectector extends Service {
	
	private SensorManager mSensorManager;
	private ShakeListener mSensorListener;
	GPStracker gps;
	String Sparrownumber="5455";
	
	public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		Log.v("GPS","SERVICE SHAKKKKKE STart");
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		int force=intent.getIntExtra("force", 80);
		int duration=intent.getIntExtra("duration", 718);
		
        mSensorListener = new ShakeListener(force,duration);   
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        mSensorListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

          public void onShake() {
        	
        	  gps = new GPStracker(ShakeDectector.this);
				 
              // check if GPS enabled     
              if(gps.canGetLocation()){
                   
                  double latitude = gps.getLatitude();
                  double longitude = gps.getLongitude();
                   
                  //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
              }else{
                  // can't get location
                  // GPS or Network is not enabled
                  // Ask user to enable GPS/network in settings
                  gps.showSettingsAlert();
              }
              
              String message=getResources().getString(R.string.messagetosend);
              String cat="Emergency";
              String type="present";
              
              String code=getJSON(type+","+cat+","+message,ShakeDectector.this);
              
              sendSMSSparrow("apikabach "+code,Sparrownumber);
              sendSMS();
              Log.v("SMS","SENT");
              Log.v("SMS","SENT TO SPARROW");
        	  
        	  
          }
          
          

          
        }); 

	    return Service.START_NOT_STICKY;
	  }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
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
          
//          Log.d("Bidhya",code);
          
  		
  		return code;
  	}
	
	public void sendSMS() {
		
		String[]phone = getResources().getStringArray(R.array.contact_array);
	   
	    String message = getResources().getString(R.string.messagetosend);
	    
	    

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
	
  //  @Override
//    protected void onResume() {
//      super.onResume();
//      mSensorManager.registerListener(mSensorListener,
//          mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//          SensorManager.SENSOR_DELAY_UI);
//    }
//
//    @Override
//    protected void onPause() {
//      mSensorManager.unregisterListener(mSensorListener);
//      super.onStop();
//    }

}
