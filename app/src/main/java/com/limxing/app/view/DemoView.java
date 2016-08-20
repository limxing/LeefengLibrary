package com.limxing.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by limxing on 16/8/20.
 */
public class DemoView extends View {
    private View view;


    Bitmap bitmap;
    Paint p = new Paint();

    public DemoView(Context context) {
        super(context);
    }

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public DemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i("leefeng",(tv==null)+"=="+tv.hashCode());
        if (bitmap != null) {
//            Log.i("leefeng",(tv==null)+"=="+tv.hashCode());
//            tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            canvas.drawBitmap(bitmap, 0, 0, p);
        }
    }

    private int w;
    private int h;


    @Override
    protected void onDetachedFromWindow() {
        if (view != null) {
            view.destroyDrawingCache();
            view = null;
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;

        }
        super.onDetachedFromWindow();
    }

    public void setText(View s) {
        view = s;
        postInvalidate();
    }

    public void removeText() {
        if (view!=null) {
            view.destroyDrawingCache();
            view = null;
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;

        }
        postInvalidate();
    }

    @Override
    public void postInvalidate() {
        if (view != null) {
//            tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            if (bitmap != null) {
                view.destroyDrawingCache();
                bitmap.recycle();
                bitmap = null;
            }
            view.measure(0, 0);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            bitmap = view.getDrawingCache();
        }
        super.postInvalidate();
    }
}
