package com.hnit.qrcode;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hnit.qrcode.Utils.HttpUtil;
import com.hnit.qrcode.Utils.QRCodeUtil;

public class MainActivity extends AppCompatActivity {
    private HttpUtil httpUtil = new HttpUtil();
    private TextView about;
    private Button tip;
    private ImageView qrCode;
    private SeekBar seekBar;
    private LinearLayout imageLayout;
    private String accNum;
    private String time;
    private String sign;
    private int width;
    private int height;
    private int seekBarLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUserData();
        //先发一次请求之后生成二维码的时候速度更快
        httpUtil.postDataWithParame(accNum, time, sign);
        initView();
    }

    //初始化用户数据
    private void initUserData() {
        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
        accNum = getSharedPreferences("userdata", MODE_PRIVATE).getString("AccNum", null);
        time = getSharedPreferences("userdata", MODE_PRIVATE).getString("Time", null);
        sign = getSharedPreferences("userdata", MODE_PRIVATE).getString("Sign", null);

        //初始化调用二维码所需要的参数
        if (accNum == null) {
            editor.putString("AccNum", "19581");
            accNum = "19581";
        }
        if (time == null) {
            editor.putString("Time", "20211030095909");
            time = "20211030095909";
        }
        if (sign == null) {
            editor.putString("Sign", "aa12396233559f6abcd48e40494d0275");
            sign = "aa12396233559f6abcd48e40494d0275";
        }

        //初始化拖动条的长度和图片长宽
        seekBarLength = getSharedPreferences("userdata", MODE_PRIVATE).getInt("seekBarLength", -1);
        if (seekBarLength == -1) {
            editor.putInt("seekBarLength", 700);
            seekBarLength = 700;
        }
        width = seekBarLength + 100;
        height = seekBarLength + 100;
        editor.apply();
    }

    //刷新二维码的统一单击监听器
    private class RefreshListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            httpUtil.postDataWithParame(accNum, time, sign);
            if(httpUtil.QRcode == null){
//                Toast.makeText(MainActivity.this, "获取失败，请检查参数是否正确", Toast.LENGTH_SHORT).show();
            }else{
                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(httpUtil.QRcode, 800, 800);
                ImageView mImageView = (ImageView) findViewById(R.id.qrcode);
                mImageView.setImageBitmap(mBitmap);
            }
        }
    }

    //”关于“按钮单击监听器
    private class AboutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showNormalDialog();
        }
    }

    //"关于"按钮长按监听器（触发“填写用户数据”）
    private class AboutLongClickListener implements View.OnLongClickListener {
        public boolean onLongClick(View v) {
            showAddDialog();
            return true;   //return true即可解决长按事件跟点击事件同时响应的问题
        }
    }

    //拖动条监听器
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (seekBar.getId() == R.id.seekbar) {
                width = i + 100;
                height = (int) (width);
                qrCode.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                editor.putInt("seekBarLength", i);
                editor.apply();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

    }

    //关于软件的说明 弹窗
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.drawable.hnitlogo1);
        normalDialog.setTitle("关于软件");
        normalDialog.setMessage("\n个人开发，解决带卡的麻烦\n\n开发者QQ 1642232305\n");
        // 显示
        normalDialog.show();
    }

    //填写用户数据 弹窗
    protected void showAddDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dailoglayout, null);
        final EditText accnumEdit = (EditText) textEntryView.findViewById(R.id.accnumEdit);
        accnumEdit.setText(accNum);
        final EditText timeEdit = (EditText) textEntryView.findViewById(R.id.timeEdit);
        timeEdit.setText(time);
        final EditText signEdit = (EditText) textEntryView.findViewById(R.id.signEdit);
        signEdit.setText(sign);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
        ad1.setTitle("填写用户数据");
        ad1.setIcon(R.drawable.hnitlogo1);
        ad1.setView(textEntryView);
        ad1.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                editor.putString("AccNum", accnumEdit.getText().toString());
                accNum = accnumEdit.getText().toString();
                editor.putString("Time", timeEdit.getText().toString());
                time = timeEdit.getText().toString();
                editor.putString("Sign", signEdit.getText().toString());
                sign = signEdit.getText().toString();
                editor.apply();
            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }

    //初始化控件
    private void initView() {
        about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(new AboutListener());
        about.setOnLongClickListener(new AboutLongClickListener());
        tip = (Button) findViewById(R.id.tip);
        tip.setOnClickListener(new RefreshListener());
        qrCode = (ImageView) findViewById(R.id.qrcode);
        qrCode.setOnClickListener(new RefreshListener());
        qrCode.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        qrCode.post(new Runnable() {
            @Override
            public void run() {
                    Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(httpUtil.QRcode, 800, 800);
                    ImageView mImageView = (ImageView) findViewById(R.id.qrcode);
                    mImageView.setImageBitmap(mBitmap);
            }
        });
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        imageLayout.setOnClickListener(new RefreshListener());
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(850);
        seekBar.setProgress(seekBarLength);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
    }

}