package com.zkc.activity;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.zkc.barcodescan.R;
import com.zkc.biz.QtsBiz;
import com.zkc.domain.MarkWithShipOrder;
import com.zkc.print.BlueToothService;
import com.zkc.util.BarcodeCreater;
import com.zkc.util.Utils;

import java.io.*;

/**
 * Created by Lynk on 14-11-27.
 */
public class PrintMarkActivity extends ParentReceiverActivity {
    private static final Point MARK_PRODUCT = new Point(420, 68);
    private static final Point SUPPLIER = new Point(420, 157);
    private static final Point LOT_NO = new Point(420, 248);
    private static final Point CODE = new Point(420, 340);

    private static final Point PCS = new Point(950, 68);
    private static final Point CASE_NO = new Point(950, 157);
    private static final Point COUNT_NO = new Point(150, 248);

    private static final Point BAR_CODE = new Point(1100, 35);
    private static final Point PRINT_TIME = new Point(1190, 0);

    private MarkWithShipOrder currentMark = null;

    private ProgressDialog progressDialog;
    private EditText uiMarkProduct;
    private EditText uiCountNo;
    private EditText uiLotNo;
    private EditText uiPcs;
    private EditText uiCaseNo;
    private EditText uiMarkCode;
    private EditText uiMarkPage;

    private BlueToothService btService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_mark);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        uiMarkProduct = (EditText) findViewById(R.id.mark_product);
        uiCountNo = (EditText) findViewById(R.id.count_no);
        uiLotNo = (EditText) findViewById(R.id.lot_no);
        uiPcs = (EditText) findViewById(R.id.pcs);
        uiCaseNo = (EditText) findViewById(R.id.case_no);
        uiMarkCode = (EditText) findViewById(R.id.mark_code);
        uiMarkPage = (EditText) findViewById(R.id.mark_page);

        btService = BlueToothService.getInstance(this);
    }

    @Override
    protected void dealCode(String code) {
        if (code.startsWith("B")) {
            new LoadMarkInfo(code).execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File rootPath = Environment.getExternalStorageDirectory();
        File savePath = new File(rootPath, "hsk_qts");
        if(savePath.exists()) {
            for (File file: savePath.listFiles()) {
                file.delete();
            }
        }
    }

    public void printOneMarkClick(View view) {
        if (currentMark == null) {
            Toast.makeText(PrintMarkActivity.this, R.string.print_no_mark, Toast.LENGTH_LONG).show();
            return;
        }
        new PrintMark(true, currentMark).execute();
    }

    public void printAllMarkClick(View view) {
        if (currentMark == null) {
            Toast.makeText(PrintMarkActivity.this, R.string.print_no_mark, Toast.LENGTH_LONG).show();
            return;
        }
        new PrintMark(false, currentMark).execute();
    }

    public class LoadMarkInfo extends AsyncTask<Void, String, MarkWithShipOrder> {
        private String boxCode;
        public LoadMarkInfo(String boxCode) {
            this.boxCode = boxCode;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected MarkWithShipOrder doInBackground (Void... voids) {
            return QtsBiz.getInstance(getApplicationContext()).getMarkWithShipOrderByBoxCode(boxCode);
        }

        @Override
        protected void onPostExecute(MarkWithShipOrder markWithShipOrder) {
            progressDialog.dismiss();
            if (markWithShipOrder == null) {
                Toast.makeText(PrintMarkActivity.this, R.string.box_code_error, Toast.LENGTH_LONG).show();
            } else {
                currentMark = markWithShipOrder;
                uiMarkProduct.setText(markWithShipOrder.getMarkProduct());
                uiCountNo.setText(markWithShipOrder.getCountNo());
                uiLotNo.setText(markWithShipOrder.getLotNo());
                uiPcs.setText(Integer.toString(markWithShipOrder.getPcs()));
                uiCaseNo.setText(markWithShipOrder.getCaseNo());
                uiMarkCode.setText(markWithShipOrder.getMarkCode());
                uiMarkPage.setText(markWithShipOrder.getMarkPage().toString());
            }
        }
    }

    private Bitmap getImageFromAssetsFile(String fileName) throws Exception {
        Bitmap image;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
            return image;
        }
        catch (IOException e) {
            Log.e("TAG", e.getMessage(), e);
            throw e;
        }
    }

    public class PrintMark extends AsyncTask<Void, String, File> {
        private int page;
        private MarkWithShipOrder mark;

        public PrintMark(boolean onePage, MarkWithShipOrder mark) {
            this.mark = mark;
            if (onePage) {
                page = 1;
            } else {
                page = mark.getMarkPage();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected File doInBackground (Void... voids) {
            Bitmap markPic = null;
            Bitmap dstMarkPic = null;
            Bitmap barCode = null;
            Bitmap dstBarCode = null;
            try {
                markPic = getImageFromAssetsFile("images/mark_japan.jpg");
                markPic = markPic.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(markPic);

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.BLACK);
                paint.setTextSize(30);
                paint.setDither(true); //获取跟清晰的图像采样
                paint.setFilterBitmap(true);//过滤一些

                canvas.drawText(mark.getMarkProduct(), MARK_PRODUCT.x, MARK_PRODUCT.y, paint);
                canvas.drawText("TAICANG HIROSE", SUPPLIER.x, SUPPLIER.y, paint);
                canvas.drawText(mark.getLotNo(), LOT_NO.x, LOT_NO.y, paint);
                canvas.drawText(mark.getMarkCode(), CODE.x, CODE.y, paint);
                canvas.drawText(Integer.toString(mark.getPcs()), PCS.x, PCS.y, paint);
                canvas.drawText(mark.getCaseNo(), CASE_NO.x, CASE_NO.y, paint);
                canvas.drawText(mark.getCountNo(), COUNT_NO.x, COUNT_NO.y, paint);

                barCode = BarcodeCreater.createBarcode(PrintMarkActivity.this, mark.getMarkCode(), 300, 55, false, 1);
                dstBarCode = roastBitMap(barCode, -90);
                canvas.drawBitmap(dstBarCode, BAR_CODE.x, BAR_CODE.y, paint);

                //当前时间
                String timeNow = Utils.getTimeNow();
                canvas.rotate(-90, PRINT_TIME.x, PRINT_TIME.y);
                paint.setTextSize(18);
                canvas.drawText(timeNow, PRINT_TIME.x - 290, PRINT_TIME.y, paint);
                canvas.rotate(90, PRINT_TIME.x, PRINT_TIME.y);

                //旋转
                dstMarkPic = roastBitMap(markPic, 90);

                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File rootPath = Environment.getExternalStorageDirectory();
                    File savePath = new File(rootPath, "hsk_qts");
                    if(!savePath.exists()) {
                        savePath.mkdir();
                    }
                    File saveFile = new File(savePath, mark.getMarkCode() + ".jpg");
                    if(saveFile.exists()) {
                        saveFile.delete();
                    }
                    OutputStream os = new FileOutputStream(saveFile);
                    dstMarkPic.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    return saveFile;
                }
            }catch (Exception e) {
                Log.e("TAG", e.getMessage());
            } finally {
                if (markPic != null && !markPic.isRecycled()) {
                    markPic.recycle();
                }
                if (dstMarkPic != null && !dstMarkPic.isRecycled()) {
                    dstMarkPic.recycle();
                }
                if (barCode != null && !barCode.isRecycled()) {
                    barCode.recycle();
                }
                if (dstBarCode != null && !dstBarCode.isRecycled()) {
                    dstBarCode.recycle();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(File markFile) {
            progressDialog.dismiss();
            if(markFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(markFile.getPath());
                for(int i = 0; i < page; i++) {
                    btService.PrintImage(bitmap, 5000);
                }
            } else {
                Toast.makeText(PrintMarkActivity.this, R.string.print_error, Toast.LENGTH_LONG).show();
            }
        }

        private Bitmap roastBitMap(Bitmap bitmap, int degrees) {
            Matrix matrix = new Matrix();
            matrix.postScale(1f, 1f);
            matrix.postRotate(degrees);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

    }
}