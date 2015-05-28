package com.zkc.receiver;

import com.zkc.activity.MainActivity;
import com.zkc.service.CaptureService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartReceiver extends BroadcastReceiver {

	public static int times = 0;
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			//��������APP
			Intent i = new Intent(context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);


			//��������service
			Intent newIntent = new Intent(context, CaptureService.class);			    	 
	    	newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(newIntent);
			
		}
	}

}
