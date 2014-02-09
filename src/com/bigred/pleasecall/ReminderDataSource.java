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
		  ReminderOpenHelper.COL_ENABLE};

  public ReminderDataSource(Context context) {
    dbHelper = new ReminderOpenHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Reminder createReminder(String uri, String description, int frequency, int enabled) {
    ContentValues values = new ContentValues();
    values.put(ReminderOpenHelper.COL_URI, uri);
    values.put(ReminderOpenHelper.COL_DESCRIPTION, description);
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

  public void deleteReminder(Reminder reminder) {
    long id = reminder.getId();
    System.out.println("Reminder deleted with id: " + id);
    database.delete(ReminderOpenHelper.REMINDERS_TABLE_NAME, ReminderOpenHelper.COL_ID
        + " = " + id, null);
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
    reminder.setEnabled(cursor.getInt(4));
    
    
    return reminder;
  }
} 