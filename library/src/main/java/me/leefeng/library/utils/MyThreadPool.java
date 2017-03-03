package me.leefeng.library.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * leefeng.me
 */
public class MyThreadPool {
    private static ExecutorService mCachedThreadPool;
    private static ExecutorService mFixedThreadPool;
    private static int THREAD_SIZE = 3;
    private static ExecutorService mScheduledThreadPool;
    private static ExecutorService mSingleThreadExecutor;

    /**
     *
     * @return
     */
    private static ExecutorService getmCachedThreadPool() {
        if (mCachedThreadPool == null) {
            mCachedThreadPool = Executors.newCachedThreadPool();
        }
        return mCachedThreadPool;
    }

    public static void excuteCachedTask(Runnable runable) {
        getmCachedThreadPool().execute(runable);
    }

    public static void submitCachedTask(Callable callable) {
        getmCachedThreadPool().submit(callable);
    }

    public static void submitCachedTask(Runnable runnable) {
        getmCachedThreadPool().submit(runnable);
    }

    /**
     *
     * @return
     */
    private static ExecutorService getmFixedThreadPool() {
        if (mFixedThreadPool == null) {

            mFixedThreadPool = Executors.newFixedThreadPool(THREAD_SIZE);
        }
        return mFixedThreadPool;
    }

    public static void excuteFixedTask(Runnable runnable) {
        getmFixedThreadPool().execute(runnable);

    }

    public static void submitFixedTask(Callable callable) {
        getmFixedThreadPool().submit(callable);
    }

    public static void submitFixedTask(Runnable runnable) {
        getmFixedThreadPool().submit(runnable);
    }

    /**
     *
     * @return
     */
    public static ExecutorService getmScheduledThreadPool() {
        if (mScheduledThreadPool == null) {
            mScheduledThreadPool = Executors.newScheduledThreadPool(THREAD_SIZE);
        }
        return mScheduledThreadPool;
    }

    /**
     *
     * @return
     */
    private static ExecutorService getmSingleThreadExecutor() {
        if (mSingleThreadExecutor == null) {
            mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        return mSingleThreadExecutor;
    }

    public static void excuteSingletask(Runnable runnable){
        getmSingleThreadExecutor().execute(runnable);
    }


}
