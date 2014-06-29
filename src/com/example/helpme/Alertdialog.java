package com.example.helpme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

///Class to take message as input and save 

@SuppressLint("ValidFragment")
public class Alertdialog extends DialogFragment{
	
	
	Context c;
	

	public Alertdialog(Context c2) {
		this.c=c2;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View myView = inflater.inflate(R.layout.message, null);
	    
	    SQLiteDatabase db=c.openOrCreateDatabase("Kabach", c.MODE_PRIVATE, null);
   	    db.execSQL("CREATE TABLE IF NOT EXISTS Message(id INT(1),message VARCHAR);");
   	    
   	    Cursor cus = db.rawQuery("SELECT message FROM Message WHERE id=1",null);
	    cus.moveToFirst();
	    String message="";
	    if(cus.getCount() > 0)
	    {
	    	message=cus.getString(0);
	    }
   	    db.close();
	    
	   EditText ed=((EditText)myView.findViewById(R.id.editText1));
	   
	   if(message.length() < 1)
		   message="Help me";
	   
	   ed.setText(message);
	   
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    
	    builder.setView(myView)
	    // Add action buttons
	           .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	String val=((EditText)myView.findViewById(R.id.editText1)).getText().toString();
	            	
	            	SQLiteDatabase db=c.openOrCreateDatabase("Kabach", c.MODE_PRIVATE, null);
	           	    db.execSQL("CREATE TABLE IF NOT EXISTS Message(id INT(1),message VARCHAR);");
	           	    Cursor cus = db.rawQuery("SELECT message FROM Message WHERE id=1",null);
	           	    cus.moveToFirst();
	           	    String query="";
	           	    if(cus.getCount() > 0)
	           	    {
	           	    	query="UPDATE Message SET message='"+val+"' WHERE id=1 ;";
	           	    }else{
	           	    	query="INSERT INTO Message VALUES('1','"+val+"');";
	           	    }
	           	    db.execSQL(query);
	           	    
	           	    db.close();
	              
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   Alertdialog.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
	
}


