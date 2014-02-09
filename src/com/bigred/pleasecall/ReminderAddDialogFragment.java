package com.bigred.pleasecall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ReminderAddDialogFragment extends DialogFragment {
	
	private static final int CONTACT_PICKER_RESULT = 1001;
	private String contact_uri = "";
	private Handler threadHandler = new Handler();
	private View view;


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		view = inflater.inflate(R.layout.dialog_add, null);
		
		((Button) view.findViewById(R.id.pick_contact)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
                doLaunchContactPicker(getView());
            }
        });
		
		builder.setView(view)
			.setTitle("Create a Reminder")
			// Add action buttons
			.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// TODO save to db
					
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					ReminderAddDialogFragment.this.getDialog().cancel();
				}
			});
		
		Spinner spinner = (Spinner) view.findViewById(R.id.frequency_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
		        R.array.frequency_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		return builder.create();
	}

	public void doLaunchContactPicker(View view) {
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
	            Contacts.CONTENT_URI);
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
            case CONTACT_PICKER_RESULT:     	
            	Uri result = data.getData();
            	Log.v("dat:", "Got a result: " + result.toString());
            	contact_uri = result.toString();
            	
            	// Will only appear if following code doesn't work
            	String displayName = "Error retrieving contact name";
            	String description = "";
            	
            	// Get the contact's name
            	int idx;
            	Cursor cursor = getActivity().getContentResolver().query(result, null, null, null, null);
            	if (cursor.moveToFirst()) {
            	    idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            	    displayName = cursor.getString(idx);
            	}
            	cursor.close();
            	
            	// Show contact name
            	TextView contactName = (TextView) view.findViewById(R.id.contactname);
            	contactName.setText("Contact: " + displayName);
            	contactName.setVisibility(View.VISIBLE);
            	
            	// Show description inputs
            	TextView descText = (TextView) view.findViewById(R.id.description);
            	descText.setVisibility(View.VISIBLE);

            	EditText desc = (EditText) view.findViewById(R.id.contactdescription);
            	desc.setVisibility(View.VISIBLE);           	
            	
                break;
            }

        } else {
            // gracefully handle failure
            Log.w("err:", "Warning: activity result not ok");
        }
        

    }
	
}
