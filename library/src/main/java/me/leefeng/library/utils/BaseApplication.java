package me.leefeng.library.utils;

import android.os.Handler;
import android.os.Looper;
import android.app.Application;

/**
 */
public class BaseApplication extends Application {

	private static BaseApplication mContext = null;
	private static Handler mMainThreadHandler = null;

	private static Thread mMainThread = null;
	private static int mMainThreadId;
	private static Looper mMainThreadLooper = null;

	@Override
	public void onCreate() {
		super.onCreate();
		this.mContext = this;
		this.mMainThreadHandler = new Handler();
		this.mMainThread = Thread.currentThread();
		this.mMainThreadId = android.os.Process.myTid();
		this.mMainThreadLooper = getMainLooper();
	}

	public static BaseApplication getApplication() {
		return mContext;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}
}