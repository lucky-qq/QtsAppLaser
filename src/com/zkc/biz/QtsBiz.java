package com.zkc.biz;

import android.content.Context;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zkc.domain.*;
import com.zkc.util.Constant;
import com.zkc.util.HttpUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lynk on 14-12-1.
 */
public class QtsBiz {
//    private static final String SERVER = "http://192.168.1.30:8090/QTS_Server";
//    private static final String SERVER = "http://10.0.2.2:8090/QTS_Server";
    private static final String PARAMS = "params";
    private static final String FLAG = "flag";
    private static final String RETURN_VALUE = "returnValue";
    private static final String FLAG_SUCCESS = "success";
    private final HttpUtil httpUtil;

    private Context context;

    private static QtsBiz qtsBiz;

    public static final QtsBiz getInstance(Context context) {
        if(qtsBiz == null) {
            qtsBiz = new QtsBiz();
        }
        qtsBiz.context = context;
        return qtsBiz;
    }

    private String getServer() {
        return "http://" + PreferenceManager.getDefaultSharedPreferences(context).getString(Constant.SERVER_PRE, "192.168.1.30") + ":8090/QTS_Server";
    }

    private QtsBiz() {
        httpUtil = new HttpUtil();
    }

    public String login(String id) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, id));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/get_employee_name_by_id", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                return jsonObj.getString(RETURN_VALUE);
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取Box信息
     * @param boxCode
     * @return
     * @throws Exception
     */
    public BoxWithBundle getBoxWithBundleByCode(String boxCode) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, boxCode));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/get_box_bundle_by_code", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                JSONObject boxJson = jsonObj.getJSONObject(RETURN_VALUE);
                return new Gson().fromJson(boxJson.toString(), BoxWithBundle.class);
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 箱子入库操作
     * @param boxCode
     * @return
     * @throws Exception
     */
    public boolean boxIn(String boxCode) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, boxCode));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/box_in", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
               return true;
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
           return false;
        }
    }

    /**
     * 作废箱子
     * @param boxCode
     * @return
     */
    public boolean boxDelete(String boxCode) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, boxCode));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/box_delete", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                return true;
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 箱子退回给检查机
     * @param boxCode
     * @return
     */
    public boolean boxBackToCheck(String boxCode) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, boxCode));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/box_back_to_check", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                return true;
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取箱号和唛头数量
     * @param boxCode
     * @return
     */
    public BoxWithMarkPage getBoxWithMarkPageByCode(String boxCode) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, boxCode));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/get_box_with_mark_page", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                JSONObject boxJson = jsonObj.getJSONObject(RETURN_VALUE);
                return new Gson().fromJson(boxJson.toString(), BoxWithMarkPage.class);
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 新增出货单
     * @param order
     * @return
     */
    public boolean createShipOrder(ShipOrderWithBoxCode order) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, new Gson().toJson(order)));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/add_ship_order", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                return true;
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 出货
     * @param orderId
     * @return
     */
    public boolean outShipOrder(String orderId) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, orderId));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/out_ship_order", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                return true;
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据箱码获得整批出货单的箱子
     * @param boxCode
     * @return
     */
    public ShipOrderWithBox getShipOrderWithBox(String boxCode) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, boxCode));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/get_ship_order_with_box", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                JSONObject orderJson = jsonObj.getJSONObject(RETURN_VALUE);
                return new Gson().fromJson(orderJson.toString(), new TypeToken<ShipOrderWithBox>(){}.getType());
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取唛头信息, 包含打印份数
     * @param boxCode
     * @return
     */
    public MarkWithShipOrder getMarkWithShipOrderByBoxCode(String boxCode) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, boxCode));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/get_mark_with_page", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                JSONObject boxJson = jsonObj.getJSONObject(RETURN_VALUE);
                return new Gson().fromJson(boxJson.toString(), MarkWithShipOrder.class);
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验二码是否匹配
     * @param box
     * @return
     */
    public boolean checkMarkBox(BoxWithMarkPage box) {
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair(PARAMS, new Gson().toJson(box)));
            JSONObject jsonObj = httpUtil.post(getServer(), "qts/add_mark_check", pairs);
            if (FLAG_SUCCESS.equals(jsonObj.getString(FLAG))) {
                return true;
            } else {
                throw new Exception(jsonObj.getString(FLAG));
            }
        } catch (Exception e) {
            return false;
        }
    }
}
