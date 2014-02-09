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
import android.database.Cursor;
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
		Toast.makeText(context, "Running poller!", Toast.LENGTH_LONG).show();
		
        ReminderDataSource dataSource = new ReminderDataSource(context);
        dataSource.open();
      
        List<Reminder> listData = dataSource.getAllReminders();
        
        for(Reminder r: listData) {
        	Log.i("update: ", r.getUri());
        	RecentCall rc = new RecentCall(context, Uri.parse(r.getUri() + ""));
        	RecentSMS rsms = new RecentSMS(context, Uri.parse(r.getUri() + ""));
        	Date dsms_tmp = rsms.getLastDate();
        	Date dcall_tmp = rc.getLastDate();
        	
        	Long dsms = new Long(0);
        	Long dcall = new Long(0);
        			
        	if(dcall_tmp != null){        		        		
        		dcall = dcall_tmp.getTime();
        		Log.i("lastcalldates: ",  dcall.toString());
        	}       	
        	
        	if(dsms_tmp != null){        		
        		dsms = dsms_tmp.getTime();
        		Log.i("lastmessagedates: ",  dsms.toString());
        	}
        	
        	int frequency = r.getFrequency();
        	if(r.getFrequency() == 15){
        		frequency = 0;
        	}
        	Long current = new Date().getTime();
        	Long dismissUntil =  new Long(0);
        	Long remindAfter = new Long(0);
        	
        	if(r.getDismissUntil() != null){
        		dismissUntil = new Date(r.getDismissUntil()).getTime();
        	}
        	if(r.getRemindAfter() != null){
        		remindAfter = new Date(r.getRemindAfter()).getTime();
        	}
        	
        	
        	Long callm = dcall + (frequency * 86400 * 1000);
        	Long smsm = dsms + (frequency * 86400 * 1000);
        	Log.i("current: ", current + "");
        	Log.i("dismissUntil: ", dismissUntil + "");
        	Log.i("remindAfter: ", remindAfter + "");
        	Log.i("callm: ", callm + "");
        	Log.i("smsm: ", smsm + "");
        	if(current > dismissUntil && current > remindAfter){
        		if(current > callm) {
        			Toast.makeText(context, "Creating notification!", Toast.LENGTH_LONG).show();
        			notification(context, r.getId(), Integer.parseInt(Uri.parse(r.getUri() + "").getLastPathSegment()));
        		}
        		if((current > smsm) && r.getSMSEnabled() == 1) {
        			Toast.makeText(context, "Creating notification!", Toast.LENGTH_LONG).show();
        			notification(context, r.getId(), Integer.parseInt(Uri.parse(r.getUri() + "").getLastPathSegment()));
        		}
        	}
        	
        	
            
//        	Log.i("notify:", "" + d);
//        	notification(context, r.getId(), Integer.parseInt(Uri.parse(r.getUri() + "").getLastPathSegment()));

        }
        
    }
	
	public void notification(Context context, long rowId, int contactID) {
			
			// Build the Uri to query to table
	        Uri contactUri = Uri.withAppendedPath(
	                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, contactID + "");
	
	        // Query the table
	        Cursor cursor = context.getContentResolver().query(
	        		contactUri, null, null, null, null);
	
	        // Get the phone numbers from the contact
	        String name = "";
	        if (cursor.moveToFirst()) {
	            name = cursor.getString(cursor
	                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

	        }
	        
			//button1--DISMISS
			Intent dismissIntent = new Intent(context.getApplicationContext(), DismissReceiver.class);
			dismissIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			dismissIntent.putExtra("id",  rowId + "");
			Log.i("extras: ", dismissIntent.getExtras().toString());
			PendingIntent piDismiss = PendingIntent.getBroadcast(context.getApplicationContext(), 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			dismissIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
			//button2--REMIND LATER	
			Intent snoozeIntent = new Intent(context.getApplicationContext(), RemindLaterReceiver.class);			
			snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			snoozeIntent.putExtra("id", rowId + "");
			PendingIntent piSnooze = PendingIntent.getBroadcast(context.getApplicationContext(), 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			NotificationCompat.Builder builder =
			        new NotificationCompat.Builder(context)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle("PleaseCall")
			        .setAutoCancel(true)
			        .setContentText(rowId + "")
			        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
			        .setStyle(new NotificationCompat.BigTextStyle()
			                .bigText("It's time to contact " + name))
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
			            (int)rowId,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			builder.setContentIntent(resultPendingIntent);
			
			NotificationManager mNotificationManager =
				    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify((int)rowId, builder.build());
		
	}
      
}