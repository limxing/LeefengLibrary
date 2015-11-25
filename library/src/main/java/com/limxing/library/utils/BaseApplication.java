package com.limxing.library.utils;

import android.os.Handler;
import android.os.Looper;
import android.app.Application;

/**
 * 基础应用类
 */
public class BaseApplication extends Application {

	// 获取到主线程的上下文
	private static BaseApplication mContext = null;
	// 获取到主线程的handler
	private static Handler mMainThreadHandler = null;

	// 获取到主线程
	private static Thread mMainThread = null;
	// 获取到主线程的id
	private static int mMainThreadId;
	// 获取到主线程的looper
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

	// 对外暴露上下文
	public static BaseApplication getApplication() {
		return mContext;
	}

	// 对外暴露主线程的handler
	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	// 对外暴露主线程
	public static Thread getMainThread() {
		return mMainThread;
	}

	// 对外暴露主线程id
	public static int getMainThreadId() {
		return mMainThreadId;
	}

	// 对外暴露主线程的looper
	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}
}