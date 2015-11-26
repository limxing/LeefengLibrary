package com.limxing.library.utils;
import android.content.Context;
import android.widget.Toast;


/**
 * 吐丝工具类
 *
 */
public class ToastUtils {
	private static Toast toastShort = null;
	private static Toast toastLong = null;
	public static void showShort(Context context, String s) {
		if (toastShort == null) {
			toastShort = Toast.makeText(context, s, Toast.LENGTH_SHORT);
			toastShort.show();
		} else {
			toastShort.setText(s);
			toastShort.show();
		}
	}
	public static void showLong(Context context, String s) {
		if (toastLong == null) {
			toastLong = Toast.makeText(context, s, Toast.LENGTH_LONG);
			toastLong.show();
		} else {
			toastLong.setText(s);
			toastLong.show();
		}
	}

}
