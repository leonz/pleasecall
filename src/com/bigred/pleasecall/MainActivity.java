package com.bigred.pleasecall;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
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
