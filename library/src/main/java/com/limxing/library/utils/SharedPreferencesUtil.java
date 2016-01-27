package com.limxing.library.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 读写SharedPreferences
 *
 * 修正: 设置了保存文件的标识，能够多用户登录时 方便使用
 */
public class SharedPreferencesUtil {
private static String info="limxing.data";
    private static SharedPreferences sharedPreferences;

    public static void saveBooleanData(Context context, String key,
                                       boolean password) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putBoolean(key, password).commit();
    }


    public static boolean getBooleanData(Context context, String key,
                                         boolean defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key,defValue);
    }


    public static void saveStringData(Context context, String key,
                                      String password) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putString(key, password).commit();
    }

    public static void saveStringData(Context context, String key,
                                      boolean password) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putBoolean(key, password).commit();
    }

    public static boolean getStringData(Context context, String key,
                                        boolean defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static String getStringData(Context context, String key,
                                       String defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }

    //保存int类型数据
    public static void saveIntData(Context context, String key, int value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }

        sharedPreferences.edit().putInt(key, value).commit();
    }

    //获取Int类型数据】
    public static int getIntData(Context context, String key, int value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }

        return sharedPreferences.getInt(key, value);
    }

}
