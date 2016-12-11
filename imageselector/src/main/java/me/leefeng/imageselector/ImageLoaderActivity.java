package me.leefeng.imageselector;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by limxing on 2016/12/11.
 */

public class ImageLoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_selectimage);
    }
}
