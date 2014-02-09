package com.bigred.pleasecall;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;


//gets the time of the last SMS message received (from a specific person)
public class RecentSMS {
	ContentResolver contentResolver = getContentResolver();
	Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ), null, null, null, null);
	boolean moved = cursor.moveToFirst(); //moved stores whether or not the cursor was moved successfully
	String date =  cursor.getString(cursor.getColumnIndex("date"));
	long timestamp = Long.parseLong(date);
	
	//Calendar calendar = Calendar.getInstance();
	//calendar.setTimeInMillis(timestamp);
	
	//Date recentDate = new Date(timestamp);
	//String recentDateStr = recentDate.toString();
	
	Calendar rightNow = Calendar.getInstance();
	Date todaysDate = rightNow.getTime();
	String todaysDateStr = todaysDate.toString();

	long milliseconds = todaysDate.getTime();
	
	long timeDifference = milliseconds - timestamp;
	
	private ContentResolver getContentResolver()  {
		// TODO Auto-generated method stub
		return null;
	}
}
