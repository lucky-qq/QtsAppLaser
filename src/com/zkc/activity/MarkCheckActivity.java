package com.zkc.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.zkc.barcodescan.R;
import com.zkc.biz.QtsBiz;
import com.zkc.domain.BoxWithMarkPage;
import com.zkc.view.MarkCheckItemAdapter;

/**
 * Created by IT on 2015-04-18.
 */
public class MarkCheckActivity extends ParentReceiverActivity {
    private ProgressDialog progressDialog;

    private MarkCheckItemAdapter markCheckItemAdapter;
    private EditText uiBoxCode;
    private EditText uiMarkPage;
    private EditText uiCheckedPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mark_check);

        progressDialog = new ProgressDialog(this);

        markCheckItemAdapter = new MarkCheckItemAdapter(this);

        uiBoxCode = (EditText) findViewById(R.id.box_code);
        uiMarkPage = (EditText) findViewById(R.id.mark_page);
        uiCheckedPage = (EditText) findViewById(R.id.checked_page);
        ListView uiMarkCode = (ListView) findViewById(R.id.mark_code);
        uiMarkCode.setAdapter(markCheckItemAdapter);
    }

    @Override
    protected void dealCode(String code) {
        if (code.startsWith("B")) {
            new LoadBoxInfo(code).execute();
        } else if (code.startsWith("M")) {
            markCheckItemAdapter.add(code);
        }
    }

    public void markCheckClick(View view) {
        if (uiBoxCode.getText().toString().length() == 0) {
            Toast.makeText(MarkCheckActivity.this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            return;
        }
        if (markCheckItemAdapter.getCount() == 0) {
            Toast.makeText(MarkCheckActivity.this, R.string.mark_code_error, Toast.LENGTH_LONG).show();
            return;
        }
        BoxWithMarkPage box = new BoxWithMarkPage();
        box.setBoxCode(uiBoxCode.getText().toString());
        box.setMarkPage(markCheckItemAdapter.getRightMarkNum());
        new AddCheck(box).execute();
    }

    public class LoadBoxInfo extends AsyncTask<Void, String, BoxWithMarkPage> {
        private String boxCode;
        public LoadBoxInfo(String boxCode) {
            this.boxCode = boxCode;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected BoxWithMarkPage doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).getBoxWithMarkPageByCode(boxCode);
        }

        @Override
        protected void onPostExecute(BoxWithMarkPage box) {
            progressDialog.dismiss();
            if (box == null) {
                Toast.makeText(MarkCheckActivity.this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            } else {
                uiBoxCode.setText(box.getBoxCode());
                uiMarkPage.setText(box.getMarkPage().toString());
                uiCheckedPage.setText(box.getCheckedPage().toString());
                markCheckItemAdapter.setBoxCode(boxCode);
            }
        }
    }

    public class AddCheck extends AsyncTask<Void, String, Boolean> {
        private BoxWithMarkPage box;

        public AddCheck(BoxWithMarkPage box) {
            this.box = box;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).checkMarkBox(box);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressDialog.dismiss();
            if (!success) {
                Toast.makeText(MarkCheckActivity.this, R.string.check_fail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MarkCheckActivity.this, R.string.check_success, Toast.LENGTH_LONG).show();
                markCheckItemAdapter.clear();
                markCheckItemAdapter.setBoxCode("");
                uiBoxCode.setText("");
                uiMarkPage.setText("");
                uiCheckedPage.setText("");
            }
        }
    }
}
