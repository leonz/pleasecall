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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		view = inflater.inflate(R.layout.dialog_add, null);
				
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
		                
		                List<Reminder> values = datasource.getAllReminders();
		                for(Reminder r: values){
		                	if (r.getUri().equals(contact_uri.toString())) {
		                		CharSequence text = "Reminder for selected contact already exists!";
								Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
								return;
		                	}		                		
		                }
		                
		                EditText desc = (EditText) view.findViewById(R.id.contactdescription);
		                
		                Spinner spinner = (Spinner) view.findViewById(R.id.frequency_spinner);
		                frequency = Integer.parseInt(spinner.getSelectedItem().toString());
		            	
		                Reminder d = datasource.createReminder(contact_uri, desc.getText().toString(), frequency, 1);
		                ((MainActivity) getActivity()).updateList();
		                
						CharSequence text = "Reminder added successfully!";
						Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
						dismiss();
					}
				}
			});
		}
	}
	
}
