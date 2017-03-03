package me.leefeng.library.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
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

    public static int getIntData(Context context, String key, int value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(info,
                    Context.MODE_PRIVATE);
        }

        return sharedPreferences.getInt(key, value);
    }

}
