package me.leefeng.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by FengTing on 2017/5/15.
 * https://www.github.com/limxing
 */

public class LoadingButton extends TextView {
    private int width;
    private Paint paint;
    private float density;
    private int height;
    private RectF bacNormal;

    public LoadingButton(Context context) {
        super(context);
        init();
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        String text= getText().toString();
        init();
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width == 0) {
            width = getWidth();
            height = getHeight();
        }
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setAlpha(120);
        if (bacNormal == null)
            bacNormal = new RectF(0, 0, width, height);
        canvas.drawRoundRect(bacNormal,5,5,paint);
        super.onDraw(canvas);
    }
}
