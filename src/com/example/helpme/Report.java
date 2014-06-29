package com.example.helpme;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Report extends DialogFragment implements OnItemSelectedListener {
	
	public String SparrowNumber="5455";
	public String voilenceMessage;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    final LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    final View reportView = inflater.inflate(R.layout.report, null);
	    
	    Spinner spinner = (Spinner) reportView.findViewById(R.id.voilencetype_spinner);
	    spinner.setOnItemSelectedListener(this);
	 // Create an ArrayAdapter using the string array and a default spinner layout
	 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(reportView.getContext(),R.array.voilencetype_array, android.R.layout.simple_spinner_item);
	 // Specify the layout to use when the list of choices appears
	 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 // Apply the adapter to the spinner
	 spinner.setAdapter(adapter);

	    
	    builder.setView(reportView)
	    // Add action buttons
	           .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText tx=(EditText) reportView.findViewById(R.id.editText1);
	            	   String message=tx.getText().toString().trim();
	            	   String rm="";
	            	   RadioGroup rg=(RadioGroup)reportView.findViewById(R.id.radioGroup1);
	            	   int sel=rg.getCheckedRadioButtonId();
	            	   if(sel==0)
	            	   {
	            		   rm="past";
	            	   }else
	            	   {
	            		   rm="present";
	            	   }
	            	   
	            	   
	            	   
	            	   
	            	   String code=getJSON(rm+","+voilenceMessage+","+message,reportView.getContext());
	            	   //Log.v("SMS","SENT TO SPARROW");
	            	   sendSMS("apikabach "+code,SparrowNumber);
	            	   
	            	   
	            	   
	             	   
	            	   
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   Report.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		voilenceMessage=getResources().getStringArray(R.array.voilencetype_array)[pos];
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void sendSMS(String message,String number) {
	    SmsManager smsManager = SmsManager.getDefault();
	    
	    smsManager.sendTextMessage(number, null, message, null, null);
	    
	    //Log.d("Bidhya", "message sent");
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
	
	

}
