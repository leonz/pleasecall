package com.bigred.pleasecall;

import java.util.ArrayList;
import java.util.Calendar;
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


//gets the time of the last SMS message received (from a specific person)
public class RecentSMS {
	
	private Context context;
	private Uri uri;
	public int lastContactedAt;
	private List<Date> dates;
	
	
	public RecentSMS(Context context, Uri uri){
		this.context = context;
		this.uri = uri;        
	}
	
	public Date getLastDate(){
		
		Uri mSmsinboxQueryUri = Uri.parse("content://sms/sent");
        Cursor cursor = context.getContentResolver().query(
                mSmsinboxQueryUri,
                new String[] { "_id", "address", "date", "body",
                        "type" }, null, null, null);

        String[] columns = new String[] { "address", "thread_id", "date",
                "body", "type" };
        dates = new ArrayList<Date>();
        while (cursor.moveToNext()) {
        	 String id = cursor.getString(0);
             String address = cursor.getString(1);
             String messsageDate = cursor.getString(2);
             String body = cursor.getString(3);
             Date messageDayTime = new Date(Long.valueOf(messsageDate));
             
             if(getContactByNumber(address).equals(uri.toString())){
            	 dates.add(messageDayTime);
             }  
        }
        
		if(dates.size() > 0){
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
