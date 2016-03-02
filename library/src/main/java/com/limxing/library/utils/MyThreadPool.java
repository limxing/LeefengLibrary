package com.limxing.library.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 作者：李利锋
 * 创建日期：2016/3/2 11:21
 * 描述：
 * <p>
 * submit的可以传递Callable的参数获取返回值，都也是自定义的错误返回
 */
public class MyThreadPool {
    private static ExecutorService mCachedThreadPool;
    private static ExecutorService mFixedThreadPool;
    private static int THREAD_SIZE = 3;//sanxingpad最大8个:Runtime.getRuntime().availableProcessors()
    private static ExecutorService mScheduledThreadPool;
    private static ExecutorService mSingleThreadExecutor;

    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
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
     * 2,创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
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
     * 3,创建一个定长线程池，支持定时及周期性任务执行。比Timer更安全，功能更强大
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
     * 4,创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     *  Android中单线程可用于数据库操作，文件操作，应用批量安装，应用批量删除等不适合并发但可能IO阻塞性及影响UI线程响应的操作。
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
