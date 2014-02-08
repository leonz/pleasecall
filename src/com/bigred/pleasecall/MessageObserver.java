package com.bigred.pleasecall;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class MessageObserver extends ContentObserver {

	public MessageObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onChange(boolean selfChange) {
		Log.i("MSG: ", "sent");
	    super.onChange(selfChange);
	    // save the message to the SD card here
	}

}
