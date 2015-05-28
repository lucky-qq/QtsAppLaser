package com.zkc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.zkc.barcodescan.R;
import com.zkc.util.Constant;

/**
 * Created by IT on 2015-04-21.
 */
public class SettingsActivity extends ParentReceiverActivity {
    private EditText uiServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        uiServer = (EditText)findViewById(R.id.server_ip);
        uiServer.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Constant.SERVER_PRE, ""));

    }

    @Override
    protected void dealCode(String code) {

    }

    public void saveSettings(View view) {
        if (uiServer.getText().toString().length() != 0) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constant.SERVER_PRE, uiServer.getText().toString()).commit();
            Toast.makeText(this, R.string.save_success, Toast.LENGTH_LONG).show();
        }
    }
}