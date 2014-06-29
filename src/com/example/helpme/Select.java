package com.example.helpme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

//Main navigation class :
//Choose:
//Set Message
//Set Contact numbers

@SuppressLint("ValidFragment")
public class Select extends DialogFragment {
	
	Context c;
	
	public Select(Context c)
	{
		this.c=c;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.setting)
	           .setItems(R.array.select, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
	            	   
	            	   if(which==0){
	            		   showMessageDialog();
	            	   }
	            	   else if(which==1){
	            		   showNumberList(c);
	            	   }else
	            	   {
	            		   //call prefrences intent;
	            		   
	            		   Intent intent = new Intent(getActivity(), Preferences.class);
	                       Select.this.startActivity(intent);
	                       
	            	   }
	            
	            
	           }
	    });
	    return builder.create();
	}
	
	
	
	public void showNumberList(Context c) {
	    DialogFragment newFragment = new Numberlist(c);
	    newFragment.show(getFragmentManager(), "number");
	}
	
	
	public void showMessageDialog() {
	    DialogFragment newFragment = new Alertdialog(c);
	    newFragment.show(getFragmentManager(), "message");
	}

	

}
