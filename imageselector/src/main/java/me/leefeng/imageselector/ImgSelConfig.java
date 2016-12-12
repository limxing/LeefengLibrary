package me.leefeng.imageselector;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limxing on 2016/12/11.
 */

public class ImgSelConfig {
    public static final int REQUEST_CODE = 999;
    public static final int RESULT_CODE = 888;

    public static int titleBacColor = 0xffffffff;
    public static int titleColor = 0xff000000;
    public static int stateColor = 0x00000000;
    public static int titleHeight;
    public static Drawable titleBackImage;
    public static boolean needCamera = false;
    //    public static ArrayList<String> array;
    public static int maxNum = 9;

    public static List<Image> checkedList;
    public static List<Image> currentList;
}
