package com.bigred.pleasecall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RemindLaterReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.i("remindlater", "e ");
		
		Toast.makeText(context, "Panik your time is up!!!!.",
			    Toast.LENGTH_LONG).show();
		
	}

}
