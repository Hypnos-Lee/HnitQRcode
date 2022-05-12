package com.hnit.qrcode.Utils;

import static android.content.ContentValues.TAG;

import android.os.Message;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static final int TIMEOUT = 3*1000;

    public static void getQRCodeData(String accNum, String time, String sign, Callback callback) {
        //创建OkHttpClient对象。
        OkHttpClient client = new OkHttpClient();
        //创建表单请求体
        FormBody.Builder formBody = new FormBody.Builder();
        //传递键值对参数
        formBody.add("AccNum", accNum);
        formBody.add("Time", time);
        formBody.add("Sign", sign);
        formBody.add("ContentType", "application/json");
        //创建Request 对象。
        Request request = new Request.Builder()
                .url("http://59.51.114.201:8080/easytong_app/getH5QRCode")
                .post(formBody.build())
                .build();
        //发送请求
        client.newCall(request).enqueue(callback);
    }

}
