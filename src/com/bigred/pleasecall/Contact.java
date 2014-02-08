package com.bigred.pleasecall;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class Contact {
	
	private String displayName;	
	private ArrayList<String> phoneNumbers = new ArrayList<String>();
	private int timesContacted;
	private int lastTimeContacted;	
	private String contactId;
	
	// Added in by the app
	private String description;
	private int daysToCall;


	
	public Contact(ContentResolver cr, Intent data, String desc, int days) {        
		
		Uri uri = data.getData();
		
        // getting contacts ID
        Cursor cursor = cr.query(uri,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
 
        if (cursor.moveToFirst()) {
            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursor.close();
		
		setDisplayName(cr, uri);
        setPhones(cr, uri);
        setTimesContacted(cr, uri);
        setLastTimeContacted(cr, uri);
 
		description = desc;
		setDaysToCall(days);
		
	}
	
    public void setPhones(ContentResolver cr, Uri uri) {
    	 
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                new String[]{contactId},
                null);
        
    	if(cursorPhone.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER) == 0) return;

    	while (cursorPhone.moveToNext()) {
            String number = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumbers.add(number);
        }     
            
        cursorPhone.close();
    }
 
    public void setDisplayName(ContentResolver cr, Uri uri) {
        Cursor cursorName = cr.query(uri, null, null, null, null);
 
        if (cursorName.moveToFirst()) {
        	displayName = cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursorName.close(); 
    }
    
    public void setTimesContacted(ContentResolver cr, Uri uri) {
        Cursor cursor = cr.query(uri, null, null, null, null);
 
        if (cursor.moveToFirst()) {
        	timesContacted = cursor.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED);
        }
        cursor.close(); 
    }
    
    public void setLastTimeContacted(ContentResolver cr, Uri uri) {
        Cursor cursor = cr.query(uri, null, null, null, null);
 
        if (cursor.moveToFirst()) {
        	lastTimeContacted = cursor.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED);
        }
        cursor.close(); 
    }
    
    public void setDaysToCall(int days) {
    	daysToCall = days;
    }
    
    public void setDescription(String desc) {
    	description = desc;
    }
    
    public int getDaysToCall() {
    	return daysToCall;
    }
    
    public int secondsToCall() {
    	return daysToCall * 24 * 60 * 60;
    }
    public String getDisplayName() {
    	return displayName;
    }
    public int getLastTimeContacted() {
    	return lastTimeContacted;
    }

    public String getDescription() {
    	return description;
    }
    
    public ArrayList<String> getPhoneNumbers() {
    	return phoneNumbers;
    }
    
    
    
}