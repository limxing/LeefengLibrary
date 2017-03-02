package com.leefeng.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.leefeng.library.NoTitleBar.SystemBarTintManager;

/**
 * Created by limxing on 16/1/19.
 */
public class MyViewpagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabviewpager);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);
    }
}
