package com.example.administrator.retrofit_rxjava.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.retrofit_rxjava.R;


public class MainActivity extends AppCompatActivity {

    private Button btn_down;                   //下载
    private Button btn_single;                 //单文件上传

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //下载
        btn_down = (Button) findViewById(R.id.btn_download);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownLoadActivity.class);
                startActivity(intent);
            }
        });

        btn_single = (Button) findViewById(R.id.btn_single);
        btn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingleUploadActivity.class);
                startActivity(intent);
            }
        });
    }

}
