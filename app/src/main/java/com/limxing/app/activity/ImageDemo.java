package com.limxing.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.limxing.app.R;
import com.limxing.library.utils.ToastUtils;

import java.util.ArrayList;

import me.leefeng.imageselector.ImageLoaderActivity;
import me.leefeng.imageselector.ImgSelConfig;

/**
 * Created by limxing on 2016/12/12.
 */

public class ImageDemo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        startActivityForResult(new Intent(this, ImageLoaderActivity.class), ImgSelConfig.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("", "onActivityResult:resultCode： " + resultCode);
        if (requestCode == me.leefeng.imageselector.ImgSelConfig.REQUEST_CODE) {
            ArrayList<String> list = data.getStringArrayListExtra("array");
            for (String s : list) {
                ToastUtils.showLong(this,s);
            }
        }
    }
    public void hah(View view) {
        startActivityForResult(new Intent(this, ImageLoaderActivity.class), ImgSelConfig.REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("", "onResume: ");
    }
}
