package com.limxing.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.limxing.utils.SystemBarTintManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemBarTintManager.initSystemBar(this,R.color.colorAccent);



    }
}
