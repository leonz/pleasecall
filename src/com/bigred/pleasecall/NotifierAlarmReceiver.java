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
 
	private Context context;
	@Override
    public void onReceive(Context context, Intent intent)
    {
		this.context = context;
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
        	notification(Integer.parseInt(Uri.parse(r.getUri() + "").getLastPathSegment()));
        	notification(1);
        	notification(2);
        	notification(3);
        }
        
    }
	
	public void notification(int contactID) {
			
			//button1--DISMISS
			Intent dismissIntent = new Intent(context, MainActivity.class);
			dismissIntent.setAction("com.example.android.pingme.ACTION_DISMISS");
			PendingIntent piDismiss = PendingIntent.getService(context, 0, dismissIntent, 0);
			
			//button2--REMIND LATER	
			Intent snoozeIntent = new Intent(context, MainActivity.class);
			snoozeIntent.setAction("com.example.android.pingme.ACTION_SNOOZE");
			PendingIntent piSnooze = PendingIntent.getService(context, 0, snoozeIntent, 0);
			
			NotificationCompat.Builder builder =
			        new NotificationCompat.Builder(context)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle("PleaseCall")
			        .setContentText("<Get name?>")
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
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			builder.setContentIntent(resultPendingIntent);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(0, builder.build());
		
	}
      
}