package com.zkc.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.zkc.barcodescan.R;
import com.zkc.biz.QtsBiz;
import com.zkc.domain.Box;
import com.zkc.domain.BoxWithBundle;

/**
 * Created by Lynk on 14-12-4.
 */
public class BoxInActivity extends ParentReceiverActivity {
    private ProgressDialog progressDialog;

    private EditText uiBoxCode;
    private EditText uiBoxCustomer;
    private EditText uiBoxProduct;
    private EditText uiBoxHeatNo;
    private EditText uiBoxTotalNum;
    private EditText uiBoxState;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_in);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        uiBoxCode = (EditText) findViewById(R.id.box_code);
        uiBoxCustomer = (EditText) findViewById(R.id.box_customer);
        uiBoxProduct = (EditText) findViewById(R.id.box_product);
        uiBoxHeatNo = (EditText) findViewById(R.id.box_heat_no);
        uiBoxTotalNum = (EditText) findViewById(R.id.box_total_num);
        uiBoxState = (EditText) findViewById(R.id.box_state);
    }

    @Override
    protected void dealCode(String code) {
        if (!code.startsWith("B") || code.length() != 11) {
            Toast.makeText(this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            return;
        }
        new LoadBoxInfo(code).execute();
    }

    public void boxInClick(View view) {
        if (uiBoxCode.getText().toString().length() != 11) {
            Toast.makeText(this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            return;
        }
        if (!Box.STATE_CHECKED.equals(uiBoxState.getText().toString())) {
            Toast.makeText(this, R.string.box_state_error, Toast.LENGTH_LONG).show();
            return;
        }
        new BoxIn().execute();
    }

    public class LoadBoxInfo extends AsyncTask<Void, String, BoxWithBundle> {
        private String boxCode;
        public LoadBoxInfo(String boxCode) {
            this.boxCode = boxCode;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected BoxWithBundle doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).getBoxWithBundleByCode(boxCode);
        }

        @Override
        protected void onPostExecute(BoxWithBundle boxWithBundle) {
            progressDialog.dismiss();
            if (boxWithBundle == null) {
                Toast.makeText(BoxInActivity.this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            } else {
                uiBoxCode.setText(boxWithBundle.getBoxCode());
                uiBoxCustomer.setText(boxWithBundle.getCustomer());
                uiBoxProduct.setText(boxWithBundle.getProduct());
                uiBoxHeatNo.setText(boxWithBundle.getHeatNo());
                uiBoxTotalNum.setText(Integer.toString(boxWithBundle.getTotalNum()));
                uiBoxState.setText(boxWithBundle.getState());
                if(Box.STATE_CHECKED.equals(boxWithBundle.getState())) {
                    uiBoxState.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    uiBoxState.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        }
    }

    public class BoxIn extends AsyncTask<Void, String, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).boxIn(uiBoxCode.getText().toString());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result) {
                uiBoxCode.setText("");
                uiBoxCustomer.setText("");
                uiBoxProduct.setText("");
                uiBoxHeatNo.setText("");
                uiBoxTotalNum.setText("");
                uiBoxState.setText("");
                Toast.makeText(BoxInActivity.this, R.string.box_in_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BoxInActivity.this, R.string.box_in_fail, Toast.LENGTH_LONG).show();
            }
        }
    }
}