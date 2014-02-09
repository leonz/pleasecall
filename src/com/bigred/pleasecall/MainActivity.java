package com.bigred.pleasecall;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int POLL_PERIOD = 600000;
	
	private ReminderDataSource dataSource;
	private ReminderAdapter listAdapter;
	private List<Reminder> listData;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // SMS listener
        ContentResolver contentResolver = getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms"), true, new MessageObserver(new Handler(), getApplicationContext()));
        
        dataSource = new ReminderDataSource(this);
        dataSource.open();
      
        listData = dataSource.getAllReminders();
        
        ListView list=(ListView)findViewById(R.id.ExpList);
        listAdapter = new ReminderAdapter(this, listData);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {
       	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// Expand			
            }
        });
        
        list.setOnItemLongClickListener(new OnItemLongClickListener() {
        	 
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	
            	final String names[] = {"Contact", "Edit","Delete"};
            	final long tag = Long.parseLong(view.getTag() + "");
            	
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.popup_list, null);                
                alertDialog.setView(convertView);                
                ListView lv = (ListView) convertView.findViewById(R.id.popup_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names){

                    @Override
                    public View getView(int position, View convertView,
                            ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);                                                
                        textView.setTextColor(Color.BLACK);
                        view.setTag(tag);
                        return view;
                    }
                };
                
                lv.setAdapter(adapter);                
                final AlertDialog dialog = alertDialog.show();
                
                lv.setOnItemClickListener(new OnItemClickListener() {
                  	 
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                            int position, long id) {
                    		dialog.dismiss();
                    		switch(position){
	                    		case 0:
	                    			Reminder r = dataSource.getReminder(Long.parseLong(view.getTag() + ""));
	                    			Intent intent = new Intent(Intent.ACTION_VIEW);
	                    			 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    			 Uri uri = Uri.withAppendedPath(Uri.parse(r.getUri()), "");
	                    			    intent.setData(uri);
	                    			 getApplicationContext().startActivity(intent);
	                    			break;
	                    		case 1:
	                    			ReminderEditDialogFragment newFragment = new ReminderEditDialogFragment();
	
	                            	Bundle args = new Bundle();            	
	                                args.putString("id", "" + view.getTag());   
	                                
	                            	newFragment.setArguments(args);
	                                newFragment.show(getFragmentManager(), "dialog");
	                                break;
	                    		case 2:
	                    			new AlertDialog.Builder(MainActivity.this)
	                    	        .setIcon(android.R.drawable.ic_dialog_alert)
	                    	        .setTitle("Delete")
	                    	        .setMessage("Are you sure you want to delete this reminder?")
	                    	        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
	
	                    	            @Override
	                    	            public void onClick(DialogInterface dialog, int which) {
	                    	            	// Delete
	                    	            	dataSource.deleteReminder(Long.parseLong(view.getTag() + ""));
	                    	            	updateList();
	                    	            }
	
	                    	        })
	                    	        .setNegativeButton("Cancel", null)
	                    	        .show();
	                    			break;
                    		}                    		
                    }
                });

				return false;
            }
        });
    }
    
    public void updateList(){
    	listAdapter.setListData(dataSource.getAllReminders());
    	
    	runOnUiThread(new Runnable() {
            @Override
            public void run() {            	
            	listAdapter.notifyDataSetChanged();
            }
        });
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
    	am.cancel(pi);
    	am.setRepeating(AlarmManager.RTC_WAKEUP,
    			SystemClock.elapsedRealtime() + POLL_PERIOD,
    			POLL_PERIOD, pi);

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
        	case R.id.action_exit:
        		 finish();
                 System.exit(0);
                 break;
    		default:    			
    			break;
        }
		return false;
    }
    
    void showDialog() {
    	ReminderAddDialogFragment newFragment = new ReminderAddDialogFragment();
        newFragment.show(getFragmentManager(), "dialog");
    }
    
}
