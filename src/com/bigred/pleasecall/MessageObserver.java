package com.bigred.pleasecall;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class MessageObserver extends ContentObserver {

	Context context;
	
	public MessageObserver(Handler handler, Context context) {
		super(handler);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onChange(boolean selfChange, Uri uri) {		
	    super.onChange(selfChange);
	    Log.i("MSG: ", "sent " + uri);
		querySMS();
	}

	protected void querySMS() {
	    Uri uriSMS = Uri.parse("content://sms/");	    
	    Cursor cur = context.getContentResolver().query(uriSMS, null, null, null, null);
	    cur.moveToNext(); // this will make it point to the first record, which is the last SMS sent
	    String body = cur.getString(cur.getColumnIndex("body")); //content of sms
	    String add = cur.getString(cur.getColumnIndex("address")); //phone num
	    String time = cur.getString(cur.getColumnIndex("date")); //date
	    String protocol = cur.getString(cur.getColumnIndex("protocol")); //protocol
	    if (protocol == null){
	        Toast.makeText(context, "Sending to "+ add +
	            ".Time:"+time +" - "+body , Toast.LENGTH_SHORT).show();
	    } else {
	    	Toast.makeText(context, "Receive from "+add +	    
	            ".Time:"+time +" - "+body , Toast.LENGTH_SHORT).show();
	    	
	    	//39 to be input from contact who 
	    	notification(39);
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
		                "tobe dismiss", piDismiss)
		        .addAction (R.drawable.ic_launcher,
		                "tobe remind later", piSnooze);
		
		 Intent intent = new Intent(Intent.ACTION_VIEW);
		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
		    intent.setData(uri);
		 //context.startActivity(intent);

		
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
