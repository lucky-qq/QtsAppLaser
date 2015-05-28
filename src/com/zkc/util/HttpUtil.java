package com.zkc.util;


import android.net.http.AndroidHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lynk on 14-5-14.
 */
public class HttpUtil {

    /**
     * post请求
     * @param server
     * @param method
     * @param pairs
     * @return
     * @throws Exception
     */
    public JSONObject post(String server, String method, List<NameValuePair> pairs) throws Exception {
        UrlEncodedFormEntity entity;
        try {
            if(pairs == null) {
                pairs = new ArrayList<>();
            }
            entity = new UrlEncodedFormEntity(pairs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Params error!");
        }
        HttpPost request = new HttpPost(server + "/" + method);
        request.setEntity(entity);
        AndroidHttpClient client = null;
        HttpResponse response;
        try {
            client =  AndroidHttpClient.newInstance("");
            response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if(code != HttpStatus.SC_OK) {
                throw new Exception("Response error code: " + code);
            }
            HttpEntity resEntity =  response.getEntity();
            if(resEntity == null) {
                return null;
            } else {
                try {
                    String rv = EntityUtils.toString(resEntity, "UTF-8");
                    return new JSONObject(rv);
                } catch (Exception e) {
                    throw new Exception("Receive error message: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new Exception("IO error: " + e.getMessage());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }
}
