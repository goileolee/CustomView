package com.goileo.cview.ac;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.goileo.cview.R;
import com.goileo.cview.view.DynamicWaveView;
import com.goileo.cview.view.GoiWaveView;

import java.lang.ref.WeakReference;

/**
 * Created by LY on 2018/4/19.
 */

public class WaveBallActivity extends AppCompatActivity {

//    private int progressInt = 0;
    private int NUMBER_COUNT = 1000;
    private static final int MSG_WHAT = 100;
    private DynamicWaveView waveView;
    private GoiWaveView goiWaveView;
    private CountHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_ball);

        waveView = (DynamicWaveView) findViewById(R.id.wave_view_dynamic);
        goiWaveView = (GoiWaveView) findViewById(R.id.wave_view_goi);

        mHandler = new CountHandler(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < NUMBER_COUNT; i+=2){
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_WHAT, i));
                    SystemClock.sleep(50);
                }
            }
        }).start();

    }

    static class CountHandler extends Handler{

        WeakReference<WaveBallActivity> activityWeak;
        public CountHandler(WaveBallActivity activity){
            activityWeak = new WeakReference<WaveBallActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WaveBallActivity waveBallActivity = activityWeak.get();
            if(msg.what == MSG_WHAT){
                waveBallActivity.waveView.setLevelNumber((int) msg.obj);
            }

        }
    }

}
