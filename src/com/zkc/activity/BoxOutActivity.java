package com.zkc.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zkc.barcodescan.R;
import com.zkc.biz.QtsBiz;
import com.zkc.domain.ShipOrderWithBox;
import com.zkc.view.BoxItemOutAdapter;

/**
 * Created by Lynk on 14-12-4.
 */
public class BoxOutActivity extends ParentReceiverActivity {
    private ProgressDialog progressDialog;

    private TextView uiOrderId;
    private TextView uiBoxNum;
    private BoxItemOutAdapter boxItemOutAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_out);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        uiOrderId = (TextView) findViewById(R.id.order_id);
        uiBoxNum = (TextView) findViewById(R.id.box_num);

        boxItemOutAdapter = new BoxItemOutAdapter(this);
        ListView uiBoxes = (ListView) findViewById(R.id.lv_box);
        uiBoxes.setAdapter(boxItemOutAdapter);
    }

    @Override
    protected void dealCode(String code) {
        if (!code.startsWith("B") || code.length() != 11) {
            Toast.makeText(this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            return;
        }
        if (uiOrderId.getText().toString().length() != 0) {
            uiOrderId.setText("");
            uiBoxNum.setText("");
            boxItemOutAdapter.clear();
        }
        new LoadShipOrder(code).execute();
    }

    public void boxOutClick(View view) {
        if (uiOrderId.getText().toString().length() != 15 || boxItemOutAdapter.getCount() == 0) {
            Toast.makeText(this, R.string.no_box_selected, Toast.LENGTH_LONG).show();
            return;
        }
        if (!boxItemOutAdapter.canOut()) {
            Toast.makeText(this, R.string.ship_order_can_not_out, Toast.LENGTH_LONG).show();
            return;
        }
        new OutShipOrder().execute();
    }

    public class LoadShipOrder extends AsyncTask<Void, String, ShipOrderWithBox> {
        private String boxCode;
        public LoadShipOrder(String boxCode) {
            this.boxCode = boxCode;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ShipOrderWithBox doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).getShipOrderWithBox(boxCode);
        }

        @Override
        protected void onPostExecute(ShipOrderWithBox order) {
            progressDialog.dismiss();
            if (order == null) {
                Toast.makeText(BoxOutActivity.this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            } else {
                uiOrderId.setText(order.getOrderId());
                uiBoxNum.setText(Integer.toString(order.getBoxes().size()));
                boxItemOutAdapter.setBoxes(order.getBoxes());
            }
        }
    }

    public class OutShipOrder extends AsyncTask<Void, String, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).outShipOrder(uiOrderId.getText().toString());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result) {
                uiOrderId.setText("");
                uiBoxNum.setText("");
                boxItemOutAdapter.clear();
                Toast.makeText(BoxOutActivity.this, R.string.box_out_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(BoxOutActivity.this, R.string.box_out_fail, Toast.LENGTH_LONG).show();
            }
        }
    }
}