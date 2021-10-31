package com.hnit.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
        about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(new AboutListener());
        tip = (TextView) findViewById(R.id.tip);
        tip.setOnClickListener(new FreshListener());
        qrCode = (ImageView) findViewById(R.id.qrcode);
        qrCode.setOnClickListener(new FreshListener());
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        imageLayout.setOnClickListener(new FreshListener());
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(850);
        seekBar.setProgress(850);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
    }

    private class FreshListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            httpUtil.postDataWithParame();
            Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(httpUtil.QRcode, 850, 850);
            ImageView mImageView = (ImageView) findViewById(R.id.qrcode);
            mImageView.setImageBitmap(mBitmap);
        }
    }
    private class AboutListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Toast toast=Toast.makeText(getApplicationContext(), "还没有写", Toast.LENGTH_SHORT);
            toast.show();
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