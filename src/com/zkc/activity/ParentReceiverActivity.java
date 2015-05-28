package com.zkc.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.zkc.util.Constant;

/**
 * Created by IT on 2015-04-24.
 */
public abstract class ParentReceiverActivity extends Activity {
    public static final int FLAG_HOME_KEY_DISPATCHED = 0x80000000; //需要自己定义标志
    private ScanBroadcastReceiver scanBroadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_HOME_KEY_DISPATCHED, FLAG_HOME_KEY_DISPATCHED);//关键代码
        scanBroadcastReceiver = new ScanBroadcastReceiver();
        intentFilter = new IntentFilter(Constant.SCAN_ACTION_NAME);
    }

    @Override
    protected void onResume() {
        if (scanBroadcastReceiver == null) {
            scanBroadcastReceiver = new ScanBroadcastReceiver();
        }
        registerReceiver(scanBroadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(scanBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("TAG", "keycode: " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                return true;
            case 4:
            case 135:
                return super.onKeyDown(keyCode, event);
            default:
                return true;
        }
    }

    /**
     * 处理代码
     * @param code
     */
    protected abstract void dealCode(String code);

    class ScanBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getExtras().getString(Constant.SCAN_CODE);
            Log.i("TAG", "MyBroadcastReceiver code:" + code);
            if (code != null && code.length() != 0) {
                dealCode(code);
            }
        }
    }
}