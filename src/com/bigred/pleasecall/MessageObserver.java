package com.bigred.pleasecall;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
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
	    Cursor cur = context.getApplicationContext().getContentResolver().query(uriSMS, null, null, null, null);
	    cur.moveToNext(); // this will make it point to the first record, which is the last SMS sent
	    String body = cur.getString(cur.getColumnIndex("body")); //content of sms
	    String add = cur.getString(cur.getColumnIndex("address")); //phone num
	    String time = cur.getString(cur.getColumnIndex("date")); //date
	    String protocol = cur.getString(cur.getColumnIndex("protocol")); //protocol
	    if (protocol == null)
	        Toast.makeText(context, "Sending to "+add +
	            ".Time:"+time +" - "+body , Toast.LENGTH_SHORT).show();
	    else Toast.makeText(context, "Receive from "+add +
	            ".Time:"+time +" - "+body , Toast.LENGTH_SHORT).show();	 
	}
}
