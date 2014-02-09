package com.bigred.pleasecall;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class NotifierAlarmReceiver extends BroadcastReceiver {
 
	@Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
    }
      
}