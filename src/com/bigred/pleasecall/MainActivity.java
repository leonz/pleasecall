package com.bigred.pleasecall;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Start SMS and phone broadcast listeners
//        Intent i= new Intent(this, MessageService.class);
//        startService(i);
//        
        // SMS
        ContentResolver contentResolver = getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"), true, new MessageObserver(new Handler(), getApplicationContext()));
    }
    
    public void onResume(){
    	super.onResume();
    	createAlarm();    
    }

    private void createAlarm(){
    	Log.i("creating", "alarm");
    	Intent i = new Intent(this, NotifierAlarmReceiver.class);
    	PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
    	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
    	am.cancel(pi); // cancel any existing alarms
//    	am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//    	    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
//    	    AlarmManager.INTERVAL_DAY, pi);
    	am.setRepeating(AlarmManager.RTC_WAKEUP,
    			SystemClock.elapsedRealtime() + 5000,
        	    15000, pi);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("BTN:", item.getItemId() + " " + R.id.action_add);
        switch(item.getItemId()){
        	case R.id.action_add:
        		Log.i("MSG: ", "Cl");
        		showDialog();
        		break;        		
        }
		return false;
    }
    
    void showDialog() {
    	ReminderAddDialogFragment newFragment = new ReminderAddDialogFragment();
        newFragment.show(getFragmentManager(), "dialog");
    }


}
