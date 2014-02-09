package com.bigred.pleasecall;

import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class NotifierAlarmReceiver extends BroadcastReceiver {
 
	@Override
    public void onReceive(Context context, Intent intent)
    {		
        // TODO Auto-generated method stub 
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
        
        ReminderDataSource dataSource = new ReminderDataSource(context);
        dataSource.open();
      
        List<Reminder> listData = dataSource.getAllReminders();
        
        for(Reminder r: listData) {
        	Log.i("update: ", r.getUri());
        	RecentCall rc = new RecentCall(context, Uri.parse(r.getUri() + ""));
        	RecentSMS rsms = new RecentSMS(context, Uri.parse(r.getUri() + ""));
        	Date dsms = rsms.getLastDate();
        	Date dcall = rc.getLastDate();
        	
        	if(dcall != null){
        		Log.i("lastcalldate: ",  dcall.toString());
        	}
        	
        	if(dsms != null){
        		Log.i("lastmessagedate: ",  dsms.toString());
        	}
        	
        	Log.i("notify:", "" + Integer.parseInt(Uri.parse(r.getUri() + "").getLastPathSegment()));
        	notification(context, Integer.parseInt(Uri.parse(r.getUri() + "").getLastPathSegment()));

        }
        
    }
	
	public void notification(Context context, int contactID) {
			
			//button1--DISMISS
			Intent dismissIntent = new Intent(context, DismissReceiver.class);
			PendingIntent piDismiss = PendingIntent.getBroadcast(context.getApplicationContext(), 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			dismissIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			dismissIntent.putExtra("id",  "sdsf");
			dismissIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
			//button2--REMIND LATER	
			Intent snoozeIntent = new Intent(context, RemindLaterReceiver.class);
			PendingIntent piSnooze = PendingIntent.getService(context.getApplicationContext(), 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			snoozeIntent.putExtra("id", contactID + "tasda");
			
			NotificationCompat.Builder builder =
			        new NotificationCompat.Builder(context)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle("PleaseCall")
			        .setContentText(contactID + "")
			        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
			        .setStyle(new NotificationCompat.BigTextStyle()
			                .bigText("bigText"))
			        .addAction (R.drawable.ic_launcher,
			                "Dismiss", piDismiss)
			        .addAction (R.drawable.ic_launcher,
			                "Remind Later", piSnooze);
			
			 Intent intent = new Intent(Intent.ACTION_VIEW);
			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
			    intent.setData(uri);
	
			
			//Intent resultIntent = new Intent(context, MainActivity.class);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(MainActivity.class);
			stackBuilder.addNextIntent(intent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            contactID,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			builder.setContentIntent(resultPendingIntent);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(contactID, builder.build());
		
	}
      
}