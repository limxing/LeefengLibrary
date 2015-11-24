package com.limxing.app;

import android.os.Bundle;

import com.limxing.library.SwipeBack.SwipeBackActivity;

import com.limxing.library.NoTitleBar.SystemBarTintManager;


public class MainActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemBarTintManager.initSystemBar(this, R.color.transparent);


    }
}
