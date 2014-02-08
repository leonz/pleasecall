package com.bigred.pleasecall;

import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class ReminderAddDialogFragment extends DialogFragment {
	
	private static final int CONTACT_PICKER_RESULT = 1001;
	private String contact_uri = "";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View view = inflater.inflate(R.layout.dialog_add, null);
		
		((Button) view.findViewById(R.id.pick_contact)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) { 
                doLaunchContactPicker(getView());
            }
        });
		
		builder.setView(view)
			.setTitle("Add reminder")
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
                break;
            }

        } else {
            // gracefully handle failure
            Log.w("err:", "Warning: activity result not ok");
        }
    }
	
}
