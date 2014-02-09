package com.bigred.pleasecall;

import java.util.List;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReminderEditDialogFragment extends DialogFragment {
	
	private static final int CONTACT_PICKER_RESULT = 1001;
	private String contact_uri = "";
	private Handler threadHandler = new Handler();
	private View view;
	boolean importSuccess = false;
	private String displayName;
	private int frequency;
	private Reminder currentReminder;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		view = inflater.inflate(R.layout.fragment_reminder_edit_dialog, null);
				
		ReminderDataSource datasource = new ReminderDataSource(getActivity());
        datasource.open();
        currentReminder = datasource.getReminder(Long.parseLong(getArguments().getString("id")));
        
        // Get the contact's name
    	int idx;
    	String name = "";
    	Cursor cursor = getActivity().getContentResolver().query(Uri.parse(currentReminder.getUri()), null, null, null, null);
    	if (cursor.moveToFirst()) {
    	    idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
    	    name = cursor.getString(idx);
    	}
    	cursor.close();     	
    	
		builder.setView(view)
			.setTitle("Modify Reminder for " + name)
			// Add action buttons
			.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// We are overriding this: see below	
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				    Toast.makeText(view.getContext(), "Fine, we won't add a reminder :(", Toast.LENGTH_LONG).show();
					ReminderEditDialogFragment.this.getDialog().cancel();
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
		
		// Set values to those of existing object
		EditText desc = (EditText) view.findViewById(R.id.contactdescription);
		desc.setText(currentReminder.getDescription());
		
		CheckBox sms_enabled = (CheckBox) view.findViewById(R.id.checktext);
		Log.i("data: ", currentReminder.getSMSEnabled() + "");
		if(currentReminder.getSMSEnabled() == 1) sms_enabled.setChecked(true);
	
		spinner.setSelection(currentReminder.getFrequency() - 1);
		
		CheckBox enabled = (CheckBox) view.findViewById(R.id.checkdisable);
		Log.i("data: ", currentReminder.getEnabled() + "");
		if(currentReminder.getEnabled() == 1) enabled.setChecked(true);
	
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
					
					// Save to database
	            	ReminderDataSource datasource = new ReminderDataSource(getActivity());
	                datasource.open();	                
	                
	                EditText desc = (EditText) view.findViewById(R.id.contactdescription);
	                
	                Spinner spinner = (Spinner) view.findViewById(R.id.frequency_spinner);
	                frequency = Integer.parseInt(spinner.getSelectedItem().toString());
	                
	                int sms_enabled = ((CheckBox) view.findViewById(R.id.checktext)).isChecked() ? 1 : 0;
	                int enabled = ((CheckBox) view.findViewById(R.id.checkdisable)).isChecked() ? 1 : 0;
	                
	                datasource.editReminder(currentReminder.getId(), currentReminder.getUri(), desc.getText().toString(), frequency, sms_enabled, enabled);
	                ((MainActivity) getActivity()).updateList();
	                
					CharSequence text = "Reminder added successfully!";
					Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
					dismiss();
					
				}
			});
		}
	}
	
}
