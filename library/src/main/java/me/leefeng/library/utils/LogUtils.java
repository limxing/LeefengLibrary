package me.leefeng.library.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * @author limxing
 */
public class LogUtils {
	public static final int LEVEL_NONE = 0;
	public static final int LEVEL_VERBOSE = 1;
	public static final int LEVEL_DEBUG = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_WARN = 4;
	public static final int LEVEL_ERROR = 5;

	private static String mTag = "limxingg";
	private static int mDebuggable = LEVEL_ERROR;

	private static long mTimestamp = 0;
	private static final Object mLogLock = new Object();

	public static void v(String msg) {
		if (mDebuggable >= LEVEL_VERBOSE) {
			Log.v(mTag, msg);
		}
	}

	public static void d(String msg) {
		if (mDebuggable >= LEVEL_DEBUG) {
			Log.d(mTag, msg);
		}
	}

	public static void i(String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(mTag, msg);
		}
	}

	public static void w(String msg) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(mTag, msg);
		}
	}

	public static void w(Throwable tr) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(mTag, "", tr);
		}
	}

	public static void w(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_WARN && null != msg) {
			Log.w(mTag, msg, tr);
		}
	}

	public static void e(String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, msg);
		}
	}

	public static void e(Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, "", tr);
		}
	}

	public static void e(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR && null != msg) {
			Log.e(mTag, msg, tr);
		}
	}

	/**
	 */
	public static void log2File(String log, String path) {
		log2File(log, path, true);
	}

	public static void log2File(String log, String path, boolean append) {
		synchronized (mLogLock) {
			FileUtils.writeFile(log + "\r\n", path, append);
		}
	}

	/**
	 */
	public static void msgStartTime(String msg) {
		mTimestamp = System.currentTimeMillis();
		if (!TextUtils.isEmpty(msg)) {
		}
	}

	public static void elapsed(String msg) {
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - mTimestamp;
		mTimestamp = currentTime;
	}

	public static <T> void printList(List<T> list) {
		if (list == null || list.size() < 1) {
			return;
		}
		int size = list.size();
		i("---begin---");
		for (int i = 0; i < size; i++) {
			i(i + ":" + list.get(i).toString());
		}
		i("---end---");
	}

	public static <T> void printArray(T[] array) {
		if (array == null || array.length < 1) {
			return;
		}
		int length = array.length;
		i("---begin---");
		for (int i = 0; i < length; i++) {
			i(i + ":" + array[i].toString());
		}
		i("---end---");
	}

	/**
	 * @param b
     */
	public static void isOnline(boolean b){
		if(b){
		 mDebuggable = LEVEL_NONE;
		}else{
			mDebuggable = LEVEL_ERROR;
		}
	}

	/**
     */
	public static void i(Activity activity, String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(mTag, activity.getClass().getSimpleName()+":"+msg);
		}
	}
	public static void i(Class classd, String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(mTag,classd.getSimpleName()+":"+msg);
		}
	}
}
