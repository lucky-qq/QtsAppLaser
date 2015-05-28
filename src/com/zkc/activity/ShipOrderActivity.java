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
import com.zkc.domain.BoxWithBundle;
import com.zkc.domain.ShipOrderWithBoxCode;
import com.zkc.view.BoxItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lynk on 14-12-4.
 */
public class ShipOrderActivity extends ParentReceiverActivity {
    private ProgressDialog progressDialog;

    private TextView uiBoxNum;
    private BoxItemAdapter uiBoxesAdapter;

    private String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ship_order);

        userName = getIntent().getStringExtra("userName");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        uiBoxNum = (TextView) findViewById(R.id.box_num);

        uiBoxesAdapter = new BoxItemAdapter(this, new BoxItemAdapter.BoxItemAction() {
            @Override
            public void afterDataSetChanged() {
                setBoxNum();
            }
        });
        ListView uiBoxes = (ListView) findViewById(R.id.lv_box);
        uiBoxes.setAdapter(uiBoxesAdapter);
    }

    @Override
    protected void dealCode(String code) {
        if (!code.startsWith("B") || code.length() != 11) {
            Toast.makeText(this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            return;
        }
        new LoadBoxInfo(code).execute();
    }

    public void shipOrderCreateClick(View view) {
        if(uiBoxesAdapter.getCount() == 0) {
            Toast.makeText(this, R.string.box_in_ship_null_error, Toast.LENGTH_LONG).show();
            return;
        }
        ShipOrderWithBoxCode order = new ShipOrderWithBoxCode();
        order.setOrderOperator(userName);
        List<String> boxCodes = new ArrayList<>();
        for(int i = 0; i < uiBoxesAdapter.getCount(); i++) {
            boxCodes.add(uiBoxesAdapter.getItem(i).getBoxCode());
        }
        order.setBoxCodes(boxCodes);

        new ShipOrderCreator(order).execute();

    }

    private void setBoxNum() {
        if (uiBoxNum != null && uiBoxesAdapter != null) {
            uiBoxNum.setText(Integer.toString(uiBoxesAdapter.getCount()));
        }
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
                Toast.makeText(ShipOrderActivity.this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            } else {
                if (!BoxWithBundle.STATE_IN_WAREHOUSE.equals(boxWithBundle.getState())) {
                    Toast.makeText(ShipOrderActivity.this, R.string.box_state_error, Toast.LENGTH_LONG).show();
                } else {
                    if (!uiBoxesAdapter.contains(boxWithBundle)) {
                        uiBoxesAdapter.add(boxWithBundle);
                    }
                }
            }
        }
    }

    public class ShipOrderCreator extends AsyncTask<Void, String, Boolean> {
        private ShipOrderWithBoxCode order;

        public ShipOrderCreator(ShipOrderWithBoxCode order) {
            this.order = order;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).createShipOrder(order);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result) {
                Toast.makeText(ShipOrderActivity.this, R.string.create_ship_order_success, Toast.LENGTH_LONG).show();
                uiBoxesAdapter.removeAll();
            } else {
                Toast.makeText(ShipOrderActivity.this, R.string.create_ship_order_fail, Toast.LENGTH_LONG).show();
            }
        }
    }
}