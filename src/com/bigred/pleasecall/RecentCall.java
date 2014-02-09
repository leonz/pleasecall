package com.bigred.pleasecall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

public class RecentCall {
	
	private Context context;
	private Uri uri;
	public int lastContactedAt;
	private List<Date> dates;

	public RecentCall(Context context, Uri uri){
		this.context = context;
		this.uri = uri;        
	}
	
	public Date getLastDate(){
		Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        
        dates = new ArrayList<Date>();
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                dir = "OUTGOING";
                break;

            case CallLog.Calls.INCOMING_TYPE:
                dir = "INCOMING";
                break;

            case CallLog.Calls.MISSED_TYPE:
                dir = "MISSED";
                break;
            }
            
            if(getContactByNumber(phNumber).equals(uri.toString())){
            	dates.add(callDayTime);
            }            
        }
        cursor.close();
        Collections.reverse(dates);
        if(dates.size() > 0) {
        	Log.i("dates:", dates.toString());
        	return dates.get(0);
        } else {
        	return null;
        }
        
	}
	
	public String getContactByNumber(String number) {
	    
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	    String name = "?";

	    ContentResolver contentResolver = context.getContentResolver();
	    Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID, 
	            ContactsContract.PhoneLookup.LOOKUP_KEY }, null, null, null);

	    String lookupKey = "", contactId = "";
	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            
	            lookupKey = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.LOOKUP_KEY));
	            contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }

	    return ContactsContract.Contacts.CONTENT_LOOKUP_URI + "/" + lookupKey + "/" + contactId;
	}
	
}
