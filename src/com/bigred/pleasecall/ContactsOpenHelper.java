package com.bigred.pleasecall;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsOpenHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	public static final String REMINDERS_TABLE_NAME = "Reminders";
	public static String COL_DESCRIPTION = "Description";
	public static String COL_FREQUENCY = "Frequency";
	public static String COL_ENABLE = "Enabled";
	public static String COL_ID = "_id";
	public static String COL_URI = "_uri";
	
	//Database creation thing
	
	//disable or enable (0 or 1)
	//unique id (key for the table)
	//contact uri 
	//frequency (a number of days/weeks/months)
	//description
	
	private static final String REMINDERS_TABLE_CREATE = 
			"CREATE TABLE " + REMINDERS_TABLE_NAME + " ( " + 
			COL_ID + " integer primary key autoincrement, " + 
			COL_DESCRIPTION + " text not null, " +
			COL_FREQUENCY + " text not null, " +
			COL_ENABLE + " integer not null, " + 
			COL_URI + " text not null " +
			");";
	
	ContactsOpenHelper(Context context) {
		super(context, REMINDERS_TABLE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REMINDERS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// do not implement
	}
}

