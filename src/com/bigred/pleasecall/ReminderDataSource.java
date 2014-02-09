package com.bigred.pleasecall;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ReminderDataSource {

  // Database fields
  private SQLiteDatabase database;
  private ReminderOpenHelper dbHelper;
  private String[] allColumns = { ReminderOpenHelper.COL_ID,
		  ReminderOpenHelper.COL_DESCRIPTION,
		  ReminderOpenHelper.COL_URI,		  
		  ReminderOpenHelper.COL_FREQUENCY,
		  ReminderOpenHelper.COL_SMSENABLE,
		  ReminderOpenHelper.COL_ENABLE,
		  ReminderOpenHelper.COL_DISMISS,
		  ReminderOpenHelper.COL_REMINDAFTER};

  public ReminderDataSource(Context context) {
    dbHelper = new ReminderOpenHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Reminder createReminder(String uri, String description, int frequency, int sms_enabled, int enabled) {
    ContentValues values = new ContentValues();
    values.put(ReminderOpenHelper.COL_URI, uri);
    values.put(ReminderOpenHelper.COL_DESCRIPTION, description);
    values.put(ReminderOpenHelper.COL_SMSENABLE, sms_enabled);
    values.put(ReminderOpenHelper.COL_FREQUENCY, frequency);
    values.put(ReminderOpenHelper.COL_ENABLE, enabled);
    long insertId = database.insert(ReminderOpenHelper.REMINDERS_TABLE_NAME, null,
        values);
    Cursor cursor = database.query(ReminderOpenHelper.REMINDERS_TABLE_NAME,
        allColumns, ReminderOpenHelper.COL_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Reminder newReminder = cursorToReminder(cursor);
    cursor.close();
    return newReminder;
  }

  public void deleteReminder(long id) {    
    System.out.println("Reminder deleted with id: " + id);
    database.delete(ReminderOpenHelper.REMINDERS_TABLE_NAME, ReminderOpenHelper.COL_ID
        + " = " + id, null);
  }
  
  public void editReminder(long id, String uri, String description, int frequency, int sms_enabled, int enabled) {
	  	ContentValues values = new ContentValues();
		values.put(ReminderOpenHelper.COL_URI, uri);
		values.put(ReminderOpenHelper.COL_DESCRIPTION, description);
		values.put(ReminderOpenHelper.COL_SMSENABLE, sms_enabled);
		values.put(ReminderOpenHelper.COL_FREQUENCY, frequency);
		values.put(ReminderOpenHelper.COL_ENABLE, enabled);
	  database.update(ReminderOpenHelper.REMINDERS_TABLE_NAME,
		        values, ReminderOpenHelper.COL_ID + "=" + id, null);
  }
  
  public void editRemindAfter(long id, String remindAfter){
	  	ContentValues values = new ContentValues();
		values.put(ReminderOpenHelper.COL_REMINDAFTER, remindAfter);
	  database.update(ReminderOpenHelper.REMINDERS_TABLE_NAME,
		        values, ReminderOpenHelper.COL_ID + "=" + id, null);
  }
  
  public void editDismiss(long id, String dismiss){
	  	ContentValues values = new ContentValues();
		values.put(ReminderOpenHelper.COL_DISMISS, dismiss);
	  database.update(ReminderOpenHelper.REMINDERS_TABLE_NAME,
		        values, ReminderOpenHelper.COL_ID + "=" + id, null);
  }

  public Reminder getReminder(long id) {
	    
	    Cursor cursor = database.query(ReminderOpenHelper.REMINDERS_TABLE_NAME,
	        allColumns, ReminderOpenHelper.COL_ID + "=" + id, null, null, null, null);

	    cursor.moveToFirst();
	    Reminder r = cursorToReminder(cursor);
	    cursor.close();
	    return r;
	    
  }
  
  public List<Reminder> getAllReminders() {
    List<Reminder> reminders = new ArrayList<Reminder>();

    Cursor cursor = database.query(ReminderOpenHelper.REMINDERS_TABLE_NAME,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Reminder reminder = cursorToReminder(cursor);
      reminders.add(reminder);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return reminders;
  }

  private Reminder cursorToReminder(Cursor cursor) {
    Reminder reminder = new Reminder();
    reminder.setId(cursor.getLong(0));
    reminder.setDescription(cursor.getString(1));
    reminder.setUri(cursor.getString(2));
    reminder.setFrequency(cursor.getInt(3));
    reminder.setSMSEnabled(cursor.getInt(4));
    reminder.setEnabled(cursor.getInt(5));
    reminder.setDismiss(cursor.getString(6));
    reminder.setRemindAfter(cursor.getString(7));
    
    return reminder;
  }
} 