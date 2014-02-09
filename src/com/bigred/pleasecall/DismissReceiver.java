package com.bigred.pleasecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DismissReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		int id = Integer.parseInt(intent.getStringExtra("id"));
//		Log.i("dimiss", "clicked " + id);
		
		Bundle extras = intent.getExtras(); 
		String userName;

		if (extras != null) {
			
		    userName = extras.getString("name");
		    // and get whatever type user account id is
		}
		Log.i("dimiss", "e " + extras);
		
		Toast.makeText(context, "Don't panik but your time is up!!!!.",
			    Toast.LENGTH_LONG).show();
		
	}

}
