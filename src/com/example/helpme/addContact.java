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
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

@SuppressLint("ValidFragment")
public class addContact extends DialogFragment {
	
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    
	Context c;
	String num;
	
	public addContact(Context c)
	{
		this.c=c;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    StrictMode.setThreadPolicy(policy);
	    
	    final View addView=inflater.inflate(R.layout.addnumbers, null);
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(addView)
	    // Add action buttons
	           .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   num=((EditText)addView.findViewById(R.id.editText1)).getText().toString();
	            	   SQLiteDatabase db=c.openOrCreateDatabase("Kabach", c.MODE_PRIVATE, null);
	            	   db.execSQL("CREATE TABLE IF NOT EXISTS Contact(Number VARCHAR);");
	            	   String query="INSERT INTO Contact VALUES('"+num+"');";
	            	   db.execSQL(query);
	            	   db.close();
	            	   addContact.this.addToDB(addView);
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   addContact.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
	
	public void addToDB(View addView)
	{
 	 
	}
	

}
