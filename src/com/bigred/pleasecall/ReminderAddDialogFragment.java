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
import android.widget.Toast;

public class ReminderAddDialogFragment extends DialogFragment {
	
	private static final int CONTACT_PICKER_RESULT = 1001;
	private String contact_uri = "";
	private Handler threadHandler = new Handler();
	private View view;
	boolean importSuccess = false;
	private String displayName;
	private int frequency;

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
					// We are overriding this: see below		
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				    Toast.makeText(view.getContext(), "Fine, we won't add a reminder :(", Toast.LENGTH_LONG).show();
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
	
	@Override
	public void onStart() {
		super.onStart();
		AlertDialog d = (AlertDialog)getDialog();
		if (d != null) {
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!importSuccess) {
						CharSequence text = "Please import a contact!";
						Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
					} else {
						// Save to database
		            	ReminderDataSource datasource = new ReminderDataSource(getActivity());
		                datasource.open();
		                
		                EditText desc = (EditText) view.findViewById(R.id.contactdescription);
		                
		                Spinner spinner = (Spinner) view.findViewById(R.id.frequency_spinner);
		                frequency = Integer.parseInt(spinner.getSelectedItem().toString());
		            	
		                Reminder d = datasource.createReminder(contact_uri, desc.getText().toString(), 2, 1);
		                ((MainActivity) getActivity()).updateList();
		                
						CharSequence text = "Reminder added successfully!";
						Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
						dismiss();
					}
				}
			});
		}
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
            	displayName = "Error retrieving contact name";
            	
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

            	importSuccess = true;
                
                break;
            }

        } else {
            // gracefully handle failure
            Log.w("err:", "Warning: activity result not ok");
        }
        

    }
	
}
