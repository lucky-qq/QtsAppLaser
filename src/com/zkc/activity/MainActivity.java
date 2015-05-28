package com.zkc.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.zkc.barcodescan.R;
import com.zkc.biz.QtsBiz;
import com.zkc.print.BlueToothService;
import com.zkc.service.CaptureService;
import com.zkc.util.Constant;
import com.zkc.view.MainMenu;
import com.zkc.view.MainMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lynk on 14-12-3.
 */
public class MainActivity extends ParentReceiverActivity {
    private static final int SET_PRINTER_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;
    private List<MainMenu> menus;
    private String userName;
    private TextView uiUserName;
    private TextView uiBluetoothMac;
    private TextView uiBluetoothInfo;

    private BlueToothService btService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        menus = new ArrayList<>();
        menus.add(new MainMenu(R.drawable.menu01, R.string.main_box_in, R.color.menu_color_1));
        menus.add(new MainMenu(R.drawable.menu02, R.string.main_box_back, R.color.menu_color_2));
        menus.add(new MainMenu(R.drawable.menu03, R.string.main_box_ship_order, R.color.menu_color_3));
        menus.add(new MainMenu(R.drawable.menu04, R.string.main_print_m, R.color.menu_color_4));
        menus.add(new MainMenu(R.drawable.menu05, R.string.main_mark_check, R.color.menu_color_5));
        menus.add(new MainMenu(R.drawable.menu06, R.string.main_box_out, R.color.menu_color_6));
        menus.add(new MainMenu(R.drawable.menu07, R.string.main_box_delete, R.color.menu_color_7));
        menus.add(new MainMenu(R.drawable.menu08, R.string.main_set_print, R.color.menu_color_8));
        menus.add(new MainMenu(R.drawable.menu09, R.string.main_setting, R.color.menu_color_9));
        MainMenuAdapter uiMenuAdapter = new MainMenuAdapter(this, menus);
        ListView uiMenu = (ListView) findViewById(R.id.menu);
        uiMenu.setAdapter(uiMenuAdapter);
        uiMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainMenuClick(i);
            }
        });

        uiUserName = (TextView) findViewById(R.id.user_name);

        uiBluetoothMac = (TextView) findViewById(R.id.bluetooth_mac);
        uiBluetoothMac.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constant.BLUETOOTH_PRINTER_ADDRESS, ""));

        uiBluetoothInfo = (TextView) findViewById(R.id.bluetooth_info);

        btService = BlueToothService.getInstance(this, new BluetoothHandler());
        if (!btService.IsOpen()) {
            btService.OpenDevice();
        } else if (uiBluetoothMac.getText().toString().length() == 17) {
            btService.ConnectToDevice(uiBluetoothMac.getText().toString());
        }

//        Intent newIntent = new Intent(MainActivity.this, CaptureService.class);
//        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startService(newIntent);
//
//        ScanBroadcastReceiver scanBroadcastReceiver = new ScanBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constant.SCAN_ACTION_NAME);
//        this.registerReceiver(scanBroadcastReceiver, intentFilter);


        Intent newIntent = new Intent(MainActivity.this, CaptureService.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(newIntent);

        //打开扫描
//        CaptureService.barCodeStr = "";
//        CaptureService.scanGpio.openScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void dealCode(String code) {
        if (code.length() != 6) {
            Toast.makeText(MainActivity.this, R.string.user_id_error, Toast.LENGTH_LONG).show();
            return;
        }
        new LoginTask().execute(code);
    }

    @Override
    public void onBackPressed() {
        final EditText uiExitCode = new EditText(this);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setView(uiExitCode)
                .setMessage(R.string.close_app_popup)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if ("88888888".equals(uiExitCode.getText().toString())) {
                                    CaptureService.scanGpio.closeScan(); //关闭扫描
                                    CaptureService.scanGpio.closePower();// 关闭电源
                                    finish();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean isDestroyed() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return super.isDestroyed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SET_PRINTER_REQUEST_CODE) {
                uiBluetoothMac.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constant.BLUETOOTH_PRINTER_ADDRESS, ""));
                if (btService.getState() == BlueToothService.STATE_CONNECTED) {
                    uiBluetoothInfo.setText(R.string.bluetooth_connected);
                    uiBluetoothInfo.setTextColor(MainActivity.this.getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    uiBluetoothInfo.setText(R.string.no_bluetooth_device);
                    uiBluetoothInfo.setTextColor(MainActivity.this.getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        }
    }

    private void mainMenuClick(int index) {
        switch (menus.get(index).getTitleResourceId()) {
            case R.string.main_box_in:
                if(userName == null) {
                    Toast.makeText(this, R.string.user_not_login_error, Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(this, BoxInActivity.class));
                break;
            case R.string.main_box_back:
                if(userName == null) {
                    Toast.makeText(this, R.string.user_not_login_error, Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(this, BoxBackActivity.class));
                break;
            case R.string.main_box_ship_order:
                if(userName == null) {
                    Toast.makeText(this, R.string.user_not_login_error, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(this, ShipOrderActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            case R.string.main_print_m:
                if(userName == null) {
                    Toast.makeText(this, R.string.user_not_login_error, Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(this, PrintMarkActivity.class));
                break;
            case R.string.main_mark_check:
                if(userName == null) {
                    Toast.makeText(this, R.string.user_not_login_error, Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(this, MarkCheckActivity.class));
                break;
            case R.string.main_box_out:
                if(userName == null) {
                    Toast.makeText(this, R.string.user_not_login_error, Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(this, BoxOutActivity.class));
                break;
            case R.string.main_box_delete:
                if(userName == null) {
                    Toast.makeText(this, R.string.user_not_login_error, Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(this, BoxDeleteActivity.class));
                break;
            case R.string.main_set_print:
                startActivityForResult(new Intent(this, SetPrinterActivity.class), SET_PRINTER_REQUEST_CODE);
                break;
            case R.string.main_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    public void loginOutClickMain(View view) {
        if(userName != null) {
            userName = null;
            uiUserName.setText(R.string.user_not_login);
            uiUserName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void connectPrintClickMain(View view) {
        if (!btService.IsOpen()) {
            btService.OpenDevice();
            return;
        }
        if (uiBluetoothMac.getText().toString().length() == 17) {
            btService.ConnectToDevice(uiBluetoothMac.getText().toString());
        } else {
            startActivityForResult(new Intent(this, SetPrinterActivity.class), SET_PRINTER_REQUEST_CODE);
        }

    }

    public class LoginTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return QtsBiz.getInstance(getApplicationContext()).login(params[0]);
        }

        @Override
        protected void onPostExecute(String name) {
            progressDialog.dismiss();
            if (name != null && name.length() > 0) {
                userName = name;
                uiUserName.setText(name);
                uiUserName.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    public class BluetoothHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case BlueToothService.SUCCESS_CONNECT:
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.connect_success), Toast.LENGTH_LONG).show();
                    uiBluetoothInfo.setText(R.string.bluetooth_connected);
                    uiBluetoothInfo.setTextColor(MainActivity.this.getResources().getColor(android.R.color.holo_green_dark));
                    break;
                case BlueToothService.FAILED_CONNECT:
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
                    uiBluetoothInfo.setText(R.string.no_bluetooth_device);
                    uiBluetoothInfo.setTextColor(MainActivity.this.getResources().getColor(android.R.color.holo_red_dark));
                    break;
            }
        }
    }
}