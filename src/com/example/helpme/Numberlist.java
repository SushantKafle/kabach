package com.example.helpme;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class Numberlist extends DialogFragment {
	
	Context c;
	
	
	@SuppressLint("ValidFragment")
	public Numberlist(Context c2) {
		// TODO Auto-generated constructor stub
		c=c2;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
	    int arraySize=-1;
	   
	    SQLiteDatabase db=c.openOrCreateDatabase("Kabach", c.MODE_PRIVATE, null);
	    db.execSQL("CREATE TABLE IF NOT EXISTS Contact(Number VARCHAR);");
	    
	    //db.execSQL("INSERT INTO Contact VALUES('9818716707');");
	    
	    Cursor cus = db.rawQuery("SELECT Number FROM Contact",null);
	    cus.moveToFirst();
	    int size=cus.getCount(); //c.getColumnIndex();
	    ArrayList<String> test=new ArrayList<String>();
	    int i=0;
	    while ( !cus.isAfterLast() ) {
	        test.add(i, cus.getString(0));
	        ++i;
	        cus.moveToNext();
	    }
	       
	    db.close();
	    String temp[]=new String[test.size()];
	    for(i=0;i<test.size();i++)
	    	temp[i]=test.get(i);
	    
	    builder.setTitle(R.string.number).
	    
	    setPositiveButton(R.string.addContact, new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int which) {
	    		addContactInterface();
	    	}
	    })
	           .setItems(temp, new DialogInterface.OnClickListener() {
	        	   //Here ^ instead of passing a constant array,we can sent a dynamic CharSequence Array.
	        	   
	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
	            	   
	            	   showEditNumberBox(which);
	            		  
	            
	            
	           }
	    });
	    return builder.create();
	}
	
	
	public void showEditNumberBox(int which) {
	    DialogFragment newFragment = new EditNumber(which);
	    newFragment.show(getFragmentManager(), "edit");
	}
	
	public void addContactInterface() {
	    DialogFragment newFragment = new addContact(c);
	    newFragment.show(getFragmentManager(), "addContact");
	}
	

}
