package com.limxing.library.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：李利锋
 * 创建日期：2016/3/2 11:21
 * 描述：创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * <p/>
 * submit的可以传递Callable的参数获取返回值，都也是自定义的错误返回
 */
public class MyThreadPool {
    private static ExecutorService mCachedThreadPool;
    private static ExecutorService mFixedThreadPool;
    private static int THREAD_SIZE=3;//sanxingpad最大8个

    private static ExecutorService getmCachedThreadPool() {
        if (mCachedThreadPool == null) {
            mCachedThreadPool = Executors.newCachedThreadPool();
        }
        return mCachedThreadPool;
    }

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
     * @return
     */
    private static ExecutorService getmFixedThreadPool(){
        if(mFixedThreadPool==null){

            mFixedThreadPool=Executors.newFixedThreadPool(THREAD_SIZE);
        }
        return mFixedThreadPool;
    }

    public static void excuteTask(Runnable runable) {
        getmCachedThreadPool().execute(runable);
    }

    public static void submitTask(Callable callable) {
        getmCachedThreadPool().submit(callable);
    }

}
