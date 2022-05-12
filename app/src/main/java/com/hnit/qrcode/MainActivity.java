package com.hnit.qrcode;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hnit.qrcode.Utils.FileUtil;
import com.hnit.qrcode.Utils.HttpUtil;
import com.hnit.qrcode.Utils.QRCodeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ImageView qrCodeImageView;
    private LinearLayout homeLayout;
    private View progressIndicator;
    private String accNum;
    private String time;
    private String sign;
    private int QRCodeSize;
    private Handler QRCodeViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化用户数据
        initUserData();

        //初始化控件
        initView();

        //二维码Handler
        QRCodeViewHandler = new Handler(msg -> {
            //生成二维码
            Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(msg.obj.toString(), QRCodeSize, QRCodeSize);
            //显示二维码
            qrCodeImageView.setImageBitmap(mBitmap);
            //关闭指示器
            progressIndicator.setVisibility(View.INVISIBLE);
            return true;
        });

        //打开软件自动生成二维码
        refreshQRCode();
    }

    //初始化用户数据
    private void initUserData() {
        //获取请求参数
        accNum = FileUtil.getString(this, "AccNum");
        time = FileUtil.getString(this, "Time");
        sign = FileUtil.getString(this, "Sign");
        //参数为空则初始化
        if (accNum == null) {
            accNum = "19581";
            FileUtil.saveString(this, "AccNum", "19581");
        }
        if (time == null) {
            time = "20211030095909";
            FileUtil.saveString(this, "Time", "20211030095909");
        }
        if (sign == null) {
            sign = "aa12396233559f6abcd48e40494d0275";
            FileUtil.saveString(this, "Sign", "aa12396233559f6abcd48e40494d0275");
        }

        //获取图片参数
        QRCodeSize = FileUtil.getInt(this, "QRCodeSize");
        if (QRCodeSize == -1) {
            QRCodeSize = 800;
            FileUtil.saveInt(this, "QRCodeSize", 800);
        }
    }

    //初始化控件
    private void initView() {
        //二维码
        qrCodeImageView = findViewById(R.id.qrcode);
        qrCodeImageView.setOnClickListener(new RefreshListener());
        //布局
        homeLayout = findViewById(R.id.homeLayout);
        homeLayout.setOnClickListener(new RefreshListener());
        //指示器
        progressIndicator = findViewById(R.id.progressIndicator);
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    //设置菜单选择事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //创建布局绑定器
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        //判断点击选项
        switch (item.getItemId()) {
            case R.id.setParameter: {
                //将布局绑定到view
                 final View parameterDialView = layoutInflater.inflate(R.layout.request_parameter_dialog, null);
                //显示对话框
                new MaterialDialog.Builder(this)
                        .title("修改参数")
                        .customView(parameterDialView, true)
                        .positiveText("确定").onPositive((dialog, which) -> {
                            //处理
                            Toast.makeText(this, "确认", Toast.LENGTH_SHORT).show();
                        })
                        .negativeText("取消")
                        .show();
                break;
            }
            //点击调整二维码
            case R.id.changeQRCode: {
                //将布局绑定到view
                final View qrCodeDialogView = layoutInflater.inflate(R.layout.qrcode_parameter_dialog, null);
                //获取对话框中两个输入框
                EditText sizeEdit = qrCodeDialogView.findViewById(R.id.QRCodeSizeEdit);
                //显示对话框
                new MaterialDialog.Builder(this)
                        .title("调整二维码")
                        .customView(qrCodeDialogView, true)
                        .positiveText("确定").onPositive((dialog, which) -> {
                            //获取大小信息
                            QRCodeSize = Integer.parseInt(sizeEdit.getText().toString());
                            //生成新的二维码
                            refreshQRCode();
                            //保存数据到本地
                            FileUtil.saveInt(this, "QRCodeSize", QRCodeSize);
                        })
                        .negativeText("取消")
                        .show();
                break;
            }
            case R.id.about: {
                new MaterialDialog.Builder(this)
                        .title("关于")
                        .content("解决带卡的麻烦\nQQ:1642232305")
                        .show();
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }


    //刷新二维码
    private void refreshQRCode() {
        HttpUtil.getQRCodeData(accNum, time, sign, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Toast.makeText(MainActivity.this, "获取二维码失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("调试信息", "获取二维码成功");
                //如果响应体为空则结束
                if (response.body() == null) {
                    Log.d("调试信息", "响应体为空");
                    return;
                }
                try {
                    //String转JSONObject
                    JSONObject responseBodyJson = new JSONObject(response.body().string());
                    //获取二维码数据
                    Message QRCodeData = new Message();
                    QRCodeData.obj = responseBodyJson.getString("qRCode");
                    //显示二维码
                    QRCodeViewHandler.sendMessage(QRCodeData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //刷新二维码的统一单击监听器
    private class RefreshListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            progressIndicator.setVisibility(View.VISIBLE);
            refreshQRCode();
        }
    }

}