package me.leefeng.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by limxing on 2017/6/21.
 */

public class ShadowImageView extends ImageView {
    public ShadowImageView(Context context) {
        super(context);
    }

    public ShadowImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float shadowWidth = 5;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       int height =  getMeasuredHeight();
        int width = getMeasuredWidth();

        Paint shadowPaint = new Paint();

        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setAntiAlias(true);
        RectF rectF = new RectF(width-shadowWidth,0,width ,height);
        canvas.drawRoundRect(rectF,0,0,shadowPaint);
        RectF bottom = new RectF(0,height - shadowWidth,width - shadowWidth,height);
        canvas.drawRoundRect(bottom,0,0,shadowPaint);



       int[] colors = new int[]{Color.parseColor("#80000000"),Color.parseColor("#30ffffff")};
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        gradientDrawable.setColors(colors);
        gradientDrawable.setBounds((int) (width-shadowWidth),0,width, (int) (height- shadowWidth));
        gradientDrawable.setCornerRadii(new float[]{0,0,shadowWidth,shadowWidth,0,0,0,0});

        gradientDrawable.draw(canvas);

        gradientDrawable = new GradientDrawable();
        gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        gradientDrawable.setColors(colors);
        gradientDrawable.setBounds(0, (int) (height - shadowWidth), (int) (width - shadowWidth),height);
        gradientDrawable.setCornerRadii(new float[]{0,0,0,0,0,0,shadowWidth,shadowWidth});

        gradientDrawable.draw(canvas);


        gradientDrawable = new GradientDrawable();
        gradientDrawable.setOrientation(GradientDrawable.Orientation.TL_BR);
        gradientDrawable.setColors(new int[]{Color.parseColor("#80000000"),Color.parseColor("#00000000"),Color.parseColor("#00000000")});
        gradientDrawable.setBounds((int)(width - shadowWidth),(int)(height - shadowWidth),width,height);
        gradientDrawable.setCornerRadii(new float[]{0,0,0,0,shadowWidth/2,shadowWidth/2,0,0});

        gradientDrawable.draw(canvas);


    }
}
