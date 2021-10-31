package com.hnit.qrcode.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    public static String QRcode = null;

    public HttpUtil() {
        postDataWithParame();
    }

    public void postDataWithParame() {
        System.out.println("执行了");
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("AccNum", "19581");//传递键值对参数
        formBody.add("Time", "20211030095909");//传递键值对参数
        formBody.add("Sign", "aa12396233559f6abcd48e40494d0275");//传递键值对参数
        formBody.add("ContentType", "application/json");//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://59.51.114.201:8080/easytong_app/getH5QRCode")
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程
                    System.out.println("获取数据成功了");
                    try {
                        //String转JSONObject
                        JSONObject result = new JSONObject(response.body().string());
                        //取数据
                        QRcode = result.getString("qRCode");
                        System.out.println(QRcode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
