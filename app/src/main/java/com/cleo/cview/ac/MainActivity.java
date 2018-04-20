package com.cleo.cview.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cleo.cview.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
    }

    private void initView() {
        findViewById(R.id.btn_count_down).setOnClickListener(this);
        findViewById(R.id.btn_wave_view).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_count_down:
                startActivity(new Intent(MainActivity.this, CountDownActivity.class));
                break;
            case R.id.btn_wave_view:
                startActivity(new Intent(MainActivity.this, WaveBallActivity.class));
                break;
        }
    }
}
