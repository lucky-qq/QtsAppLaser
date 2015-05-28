package com.zkc.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;
import com.zkc.barcodescan.R;
import com.zkc.print.BlueToothService;
import com.zkc.util.Constant;

import java.util.Set;

/**
 * Created by Lynk on 14-12-3.
 */
public class SetPrinterActivity extends ParentReceiverActivity {

    private ArrayAdapter<String> uiDevicesAdapter;
    private TextView uiProgressInfo;
    private ProgressBar uiProgressBar;

    private BlueToothService btService = null;

    private String printerAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_printer);

        uiDevicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1);
        ListView uiDevices = (ListView) findViewById(R.id.bluetooth_device);
        uiDevices.setAdapter(uiDevicesAdapter);
        uiDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!btService.IsOpen()) {
                    btService.OpenDevice();
                    return;
                }
                if (btService.GetScanState() == BlueToothService.STATE_SCANING) {
                    btService.StopScan();
                }
                if (btService.getState() == BlueToothService.STATE_CONNECTING) {
                    return;
                }

                String info = ((TextView) view).getText().toString();
                printerAddress = info.substring(info.length() - 17);
                btService.DisConnected();
                btService.ConnectToDevice(printerAddress);// 连接蓝牙
            }
        });


        uiProgressInfo = (TextView) findViewById(R.id.progress_info);
        uiProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        btService = BlueToothService.getInstance(this, new BluetoothHandler());
        btService.setOnReceive(new BlueToothService.OnReceiveDataHandleEvent() {

            @Override
            public void OnReceive(BluetoothDevice device) {
                if (device != null) {
                    uiDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                    uiDevicesAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    protected void dealCode(String code) {

    }

    public void openBluetooth(View view) {
        if (!btService.IsOpen()) {
            btService.OpenDevice();
        }
    }

    public void scanBluetooth(View view) {
        if (!btService.IsOpen()) {
            btService.OpenDevice();
            return;
        }
        if (btService.GetScanState() == BlueToothService.STATE_SCANING) {
            return;
        }
        uiDevicesAdapter.clear();
        uiProgressInfo.setVisibility(View.VISIBLE);
        uiProgressBar.setVisibility(View.VISIBLE);
        Set<BluetoothDevice> bondedDevice = btService.GetBondedDevice();
        if (bondedDevice.size() > 0) {
            for (BluetoothDevice device : bondedDevice) {
                uiDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        uiDevicesAdapter.notifyDataSetChanged();
        new Thread() {
            public void run() {
                btService.ScanDevice();
            }
        }.start();
    }

    public void disconnect(View view) {
        btService.DisConnected();
    }

    public class BluetoothHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case BlueToothService.SUCCESS_CONNECT:
                    Toast.makeText(SetPrinterActivity.this, SetPrinterActivity.this.getResources().getString(R.string.connect_success), Toast.LENGTH_LONG).show();
                    uiProgressInfo.setVisibility(View.GONE);
                    uiProgressBar.setVisibility(View.GONE);
                    PreferenceManager.getDefaultSharedPreferences(SetPrinterActivity.this).edit().putString(Constant.BLUETOOTH_PRINTER_ADDRESS, printerAddress).commit();
                    setResult(RESULT_OK, new Intent(SetPrinterActivity.this, MainActivity.class));
                    finish();
                    break;
                case BlueToothService.FAILED_CONNECT:
                    Toast.makeText(SetPrinterActivity.this, SetPrinterActivity.this.getResources().getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
                    uiProgressInfo.setVisibility(View.GONE);
                    uiProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }
}