package com.bigred.pleasecall;

import java.util.Date;

import android.app.NotificationManager;
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
		long id = Long.parseLong(intent.getStringExtra("id"));
		
		ReminderDataSource dataSource = new ReminderDataSource(context);
        dataSource.open();
      
        Reminder r = dataSource.getReminder(id);
        Date dismissUntil = new Date(System.currentTimeMillis() + (r.getFrequency() * 86400 * 1000));
        dataSource.editDismiss(id, dismissUntil.toString());
        
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel((int)id);
		
	}

}
