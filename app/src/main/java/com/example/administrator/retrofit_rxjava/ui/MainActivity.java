package com.example.administrator.retrofit_rxjava.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.retrofit_rxjava.R;


public class MainActivity extends AppCompatActivity {

    private Button btn_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_down = (Button) findViewById(R.id.btn_download);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownLoadActivity.class);
                startActivity(intent);
            }
        });
    }

}
