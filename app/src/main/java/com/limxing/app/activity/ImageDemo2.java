package com.limxing.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.limxing.app.R;

/**
 * Created by limxing on 2016/12/12.
 */

public class ImageDemo2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setResult(RESULT_OK);
        finish();
    }
}
