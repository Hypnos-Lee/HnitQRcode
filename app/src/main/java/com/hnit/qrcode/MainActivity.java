package com.hnit.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hnit.qrcode.Utils.HttpUtil;
import com.hnit.qrcode.Utils.QRCodeUtil;

public class MainActivity extends AppCompatActivity {
    HttpUtil httpUtil = new HttpUtil();
    TextView tip,about;
    ImageView qrCode;
    SeekBar seekBar;
    LinearLayout imageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tip = (TextView) findViewById(R.id.tip);
        tip.setOnClickListener(new ClickListener());
        about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(new ClickListener());
        qrCode = (ImageView) findViewById(R.id.qrcode);
        qrCode.setOnClickListener(new ClickListener());
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        imageLayout.setOnClickListener(new ClickListener());
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(800);
        seekBar.setProgress(800);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            httpUtil.postDataWithParame();
            Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(httpUtil.QRcode, 850, 850);
            ImageView mImageView = (ImageView) findViewById(R.id.qrcode);
            mImageView.setImageBitmap(mBitmap);
        }

    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (seekBar.getId() == R.id.seekbar) {
                int newWidth = i + 100;
                int newHeight = (int) (newWidth);//按照原图片的比例缩放
                qrCode.setLayoutParams(new LinearLayout.LayoutParams(newWidth, newHeight));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

    }


}