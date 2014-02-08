package com.bigred.pleasecall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotifierService extends Service {
	public NotifierService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
