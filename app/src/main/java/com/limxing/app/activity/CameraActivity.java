package com.limxing.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.limxing.app.MainActivity;
import com.limxing.app.R;
import com.limxing.library.utils.BitmapHelper;
import com.limxing.library.utils.DisplayUtil;

import net.bither.util.NativeUtil;

import java.io.File;

/**
 * Created by limxing on 16/3/3.
 */
public class CameraActivity extends AppCompatActivity {
    private static final int CAMERA = 0;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues values = new ContentValues();
                photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA) {
            /**
             * 华为手机在横着拍竖着拍，回调时，无论照片是正是反都崩溃
             * 小米pad 竖着拍横着照相，崩溃，横着拍竖着照 崩溃
             * 中兴：  横着竖着拍 崩溃
             * 三星pad:横着拍竖着照 崩溃
             *
             * 以上崩溃的原因就是photoUri被重置为null，因此在配置文件中的activity中添加
             *  android:configChanges="keyboardHidden|orientation|screenSize"
             * 锁定屏幕也能够解决这个问题
             * android:screenOrientation="portrait" 竖屏
             * android:screenOrientation="landscape" 横屏
             *
             */
            Cursor cursor = getContentResolver().query(photoUri, null,
                    null, null, null);
            cursor.moveToFirst();
            String q1 = cursor.getString(1); // 图片文件路径
            cursor.close();
            Log.i("limxing", q1);
            File qq = new File(q1);

            //尺寸剪切后,
            String path = BitmapHelper.compressBitmap(CameraActivity.this, q1, DisplayUtil.getScreenWith(CameraActivity.this),
                    DisplayUtil.getScreenHeight(CameraActivity.this), false);
            Log.i("limxing", path);
            //质量压缩
            File file = new File(BitmapHelper.getImageCacheDir(CameraActivity.this));
            String s = file.toString() + "/NativeUtil_" + qq.getName();
            NativeUtil.compressBitmap(BitmapFactory.decodeFile(path),80, s, true);//剪切后质量压缩


//            String ss = s + ".jpg";
//            BitmapHelper.compressBitmap(MainActivity.this, ss, screenWidth, screenHeigh, );

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
