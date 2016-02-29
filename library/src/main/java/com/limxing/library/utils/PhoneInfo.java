package com.limxing.library.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by limxing on 16/2/29.
 */
public class PhoneInfo {

    public static void show(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels= dm.widthPixels;
        int heightPixels= dm.heightPixels;
        float density = dm.density;
        int densityDpi = dm.densityDpi;
//        int screenWidth = (int) (widthPixels * density);
//        int screenHeight = (int) (heightPixels * density);
        LogUtils.i("screenWidth:"+widthPixels);
        LogUtils.i("screenHeight:"+heightPixels);
        LogUtils.i("densityDpi:"+densityDpi);
        LogUtils.i("densityDpi:"+density);
    }
}
