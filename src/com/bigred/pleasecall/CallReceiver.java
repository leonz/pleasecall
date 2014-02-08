package com.bigred.pleasecall;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public  class CallReceiver extends BroadcastReceiver {
    
	@Override
    public void onReceive(Context context, Intent intent) {
		PhoneStateChangeListener pscl = new PhoneStateChangeListener();
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(pscl, PhoneStateListener.LISTEN_CALL_STATE);
    }
	
	private class PhoneStateChangeListener extends PhoneStateListener {
	    public boolean wasRinging;
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	    	Log.i("Ch", "asdfadsfsdf");
//	        switch(state){
//	            case TelephonyManager.CALL_STATE_RINGING:
//	                 Log.i("Ch", "RINGING");
//	                 wasRinging = true;
//	                 break;
//	            case TelephonyManager.CALL_STATE_OFFHOOK:
//	                 Log.i("Ch", "OFFHOOK");
//
//	                 if (!wasRinging) {
//	                     // 
//	                 } else {
//	                     // Cancel your old activity
//	                 }
//
//	                 // this should be the last piece of code before the break
//	                 wasRinging = true;
//	                 break;
//	            case TelephonyManager.CALL_STATE_IDLE:
//	                 Log.i("Ch", "IDLE");
//	                 // this should be the last piece of code before the break
//	                 wasRinging = true;
//	                 break;
//	        }
	    }
	}
}

  