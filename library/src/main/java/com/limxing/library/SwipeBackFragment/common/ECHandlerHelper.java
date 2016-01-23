package com.limxing.library.SwipeBackFragment.common;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by limxing on 16/1/23.
 */
public class ECHandlerHelper {
    private static Handler b = null;

    private static Handler a() {
        if (b == null)
            b = new Handler(Looper.getMainLooper());
        return b;
    }


    public static void postRunnOnUI(Runnable paramRunnable) {
        if (paramRunnable == null)
            return;
        a().post(paramRunnable);
    }

}
