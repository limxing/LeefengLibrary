package com.leefeng.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.leefeng.app.R;
import me.leefeng.library.utils.BitmapHelper;
import me.leefeng.library.utils.DisplayUtil;

import net.bither.util.NativeUtil;

import java.io.File;

/**
 * Created by limxing on 16/3/3.
 */
public class CameraActivity extends AppCompatActivity {
    private static final int CAMERA = 0;
    private static final int CAMERA_RESULT = 2;
    private static final int RESULT_LOAD_IMAGE = 3;
    private Uri photoUri;
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/**
 * 图片选择
 */
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                //小米2不支持
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                ContentValues values = new ContentValues();
//                photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
//                startActivityForResult(intent, CAMERA);
//                String state = Environment.getExternalStorageState();
//                if (state.equals(Environment.MEDIA_MOUNTED)) {
//                    mPhotoFile = new File(FileUtils.getCacheDir(), System.currentTimeMillis() + ".jpg");
//
//                    if (!mPhotoFile.exists()) {
//                        try {
//                            mPhotoFile.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplication(), "照片创建失败!",
//                                    Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    }
//                    Intent intent = new Intent(
//                            "android.media.action.IMAGE_CAPTURE");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                            Uri.fromFile(mPhotoFile));
//                    startActivityForResult(intent, CAMERA_RESULT);
//                } else {
//                    Toast.makeText(getApplication(), "sdcard无效或没有插入!",
//                            Toast.LENGTH_SHORT).show();
//                }
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


            File qq = new File(q1);

            //尺寸剪切后,
            String path = BitmapHelper.compressBitmap(CameraActivity.this, q1, DisplayUtil.getScreenWith(CameraActivity.this),
                    DisplayUtil.getScreenHeight(CameraActivity.this), false);

            //质量压缩
            File file = new File(BitmapHelper.getImageCacheDir(CameraActivity.this));
            String s = file.toString() + "/NativeUtil_" + qq.getName();
            NativeUtil.compressBitmap(path, 50, s, true, true);//剪切后质量压缩,最后一个参数是否删除原文件


        }

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_RESULT) {

            //尺寸剪切后,
            String path = BitmapHelper.compressBitmap(CameraActivity.this, mPhotoFile.getAbsolutePath(),
                    DisplayUtil.getScreenWith(CameraActivity.this),
                    DisplayUtil.getScreenHeight(CameraActivity.this), false);

            //质量压缩
            File file = new File(BitmapHelper.getImageCacheDir(CameraActivity.this));
            String s = file.toString() + "/NativeUtil_" + mPhotoFile.getName();
            NativeUtil.compressBitmap(path, 50, s, true, true);//剪切后质量压缩,最后一个参数是否删除原文件


        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
