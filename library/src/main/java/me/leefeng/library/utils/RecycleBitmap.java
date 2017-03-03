package me.leefeng.library.utils;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

public class RecycleBitmap {

    /**
     * 清理map中的bitmap;
     *
     * @param imgCache ImageCacheMap
     * @param freeSize 释放掉图片的数量;
     */
    public static void recycleMapCache(LinkedHashMap<String, Bitmap> imgCache, int maxSize, int freeSize) {
        if (imgCache.values().size() > maxSize) {
            synchronized (imgCache) {
                Iterator<String> it = imgCache.keySet().iterator();
                while (it.hasNext() && (imgCache.keySet().size() > freeSize)) {
                    Bitmap bmp = imgCache.get(it.next());
                    if (bmp != null && !bmp.isRecycled()) {
                        bmp.recycle();
                        bmp = null;
                    }
                    it.remove();
                }
            }
            System.gc();
        }
    }


    /**
     *
     */
    public static void recycle(Map<View, int[]> mapViews) {
        synchronized (mapViews) {
            Iterator<View> it = mapViews.keySet().iterator();
            while (it.hasNext()) {
                //获取布局
                View view = it.next();
                if (view == null) return;
                int[] recycleIds = mapViews.get(view);

                if ((view instanceof AbsListView)) {
                    recycleAbsList((AbsListView) view, recycleIds);
                }
                //如果是ImageView,直接回收;
                else if (view instanceof ImageView) {
                    recycleImageView(view);
                }
                //如果是ViewGroup,重要提示:每个ImagView在ViewGroup的第二层;
                else if ((view instanceof ViewGroup)) {
                    recycleViewGroup((ViewGroup) view, recycleIds);
                }
            }
        }
        System.gc();
    }


    /**
     */
    public static void recycleAbsList(AbsListView absView, int[] recycleIds) {
        if (absView == null) return;
        synchronized (absView) {
            for (int index = absView.getFirstVisiblePosition(); index <= absView.getLastVisiblePosition(); index++) {
                ViewGroup views = (ViewGroup) absView.getAdapter().getView(index, null, absView);
                for (int count = 0; count < recycleIds.length; count++) {
                    recycleImageView(views.findViewById(recycleIds[count]));
                }
            }
        }
    }


    /**
     */
    public static void recycleViewGroup(ViewGroup layout, int[] recycleIds) {
        if (layout == null) return;
        synchronized (layout) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View subView = layout.getChildAt(i);
                if (subView instanceof ViewGroup) {
                    for (int count = 0; count < recycleIds.length; count++) {
                        recycleImageView(subView.findViewById(recycleIds[count]));
                    }
                } else {
                    if (subView instanceof ImageView) {
                        recycleImageView((ImageView) subView);
                    }
                }
            }
        }
    }


    /**
     *
     * @param view
     */
    public static void recycleImageView(View view) {
        if (view == null) return;
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()) {
//                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                    bmp = null;
                }
            }
        }
    }
}
