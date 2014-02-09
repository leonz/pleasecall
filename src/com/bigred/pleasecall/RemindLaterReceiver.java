package com.bigred.pleasecall;

import java.util.Date;

import android.app.NotificationManager;
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
		long id = Long.parseLong(intent.getStringExtra("id"));
		Log.i("dimiss", "i: " + id);
	
		ReminderDataSource dataSource = new ReminderDataSource(context);
        dataSource.open();
      
        Reminder r = dataSource.getReminder(id);
        Date remindAfter = new Date(System.currentTimeMillis() + (4 * 3600 * 1000));
        dataSource.editRemindAfter(id, remindAfter.toString());
        
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel((int)id);
		
	}

}
