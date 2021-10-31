package com.hnit.qrcode.Utils;

import android.os.Message;

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
    }

    public void postDataWithParame(String accNum, String time, String sign) {
        System.out.println("发起了POST异步请求");
        System.out.println("AccNum=" + accNum);
        System.out.println("Time=" + time);
        System.out.println("Sign" + sign);
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("AccNum", accNum);//传递键值对参数
        formBody.add("Time", time);//传递键值对参数
        formBody.add("Sign", sign);//传递键值对参数
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

    public void postDatasync(String accNum, String time, String sign){
        System.out.println("发起了POST同步请求");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                    formBody.add("AccNum", accNum);//传递键值对参数
                    formBody.add("Time", time);//传递键值对参数
                    formBody.add("Sign", sign);//传递键值对参数
                    formBody.add("ContentType", "application/json");//传递键值对参数
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")//请求接口。如果需要传参拼接到接口后面。
                            .post(formBody.build())//传递请求体
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        System.out.println("response.code()=="+response.code());
                        System.out.println("response.message()=="+response.message());
                        System.out.println("res=="+response.body().string());
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
