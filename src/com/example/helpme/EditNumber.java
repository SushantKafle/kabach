package com.example.helpme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

@SuppressLint("ValidFragment")
public class EditNumber extends DialogFragment {
	
	
	private int index;
	
	public EditNumber(int which) {
		// TODO Auto-generated constructor stub
		index=which;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    //Get the Contact that was selected and display it for edit.
	    //It is saved in number.
	    
	    String number=getResources().getStringArray(R.array.contact_array)[index];
	    //Now that we have the number, we have to get the View and add the number to the editText
	    View editView = inflater.inflate(R.layout.editnumbers, null);
	    EditText tx=(EditText)editView.findViewById(R.id.editText1);
	    tx.setText(number);
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    
	    builder.setView(editView)
	    // Add action buttons
	           .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	   
	              
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   EditNumber.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
	
	

}
