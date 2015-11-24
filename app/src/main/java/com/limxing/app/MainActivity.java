package com.limxing.app;

import android.os.Bundle;

import com.limxing.NoTitleBar.SystemBarTintManager;
import com.limxing.SwipeBack.SwipeBackActivity;

public class MainActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemBarTintManager.initSystemBar(this, R.color.transparent);


    }
}
