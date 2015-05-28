package com.zkc.receiver;

import com.zkc.service.CaptureService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("ShutdownReceiver", "�ػ���Ϣ,�ر������Դ.........");
		CaptureService.scanGpio.closePower();
	}
}