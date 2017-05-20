package me.leefeng.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import me.leefeng.library.R;

/**
 * Created by limxing on 2017/5/20.
 */

public class SetView extends View {
//    private static final String TAG = "SetView";
    private Paint paint;
    private float density;
    private int titleColor;
    private float titleSize;
    private String titleText = "标题";
    private Rect textRect;
    private boolean showTopLine;
    private int dividerColor;
    private float dividerWidth;
    private float dividerTopLeft;
    private float dividerTopRight;
    private float dividerBottomLeft;
    private float dividerBottomRight;
    private boolean showBottomLine;
    private Drawable rightPic;

    public SetView(Context context) {
        super(context);

    }

    public SetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        textRect = new Rect();
        density = getResources().getDisplayMetrics().density;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SetView);
        titleColor = typedArray.getColor(R.styleable.SetView_setTitleColor, Color.BLACK);
        titleSize = typedArray.getDimension(R.styleable.SetView_setTitleSize, 16 * density);
        titleText = typedArray.getString(R.styleable.SetView_setTitleText);
        showTopLine = typedArray.getBoolean(R.styleable.SetView_setShowTopLine, true);
        dividerColor = typedArray.getColor(R.styleable.SetView_setDividerColor, Color.parseColor("#eeeeee"));
        dividerWidth = typedArray.getDimension(R.styleable.SetView_setDividerWidth, 1 * density);
        dividerTopLeft = typedArray.getDimension(R.styleable.SetView_setTopDividerLeft, 0);
        dividerTopRight = typedArray.getDimension(R.styleable.SetView_setTopDividerRight, 0);
        showBottomLine = typedArray.getBoolean(R.styleable.SetView_setShowBottomLine, true);
        dividerBottomLeft = typedArray.getDimension(R.styleable.SetView_setBottomDividerLeft, 0);
        dividerBottomRight = typedArray.getDimension(R.styleable.SetView_setBottomDividerRight, 0);

        rightPic = typedArray.getDrawable(R.styleable.SetView_setRightPic);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        if (showTopLine) {
            paint.reset();
            paint.setColor(dividerColor);
            paint.setStrokeWidth(dividerWidth);
            paint.setAntiAlias(true);

            canvas.drawLine(dividerTopLeft, dividerWidth / 2, getWidth() - dividerTopRight, dividerWidth / 2, paint);

        }
        if (showBottomLine) {
            paint.reset();
            paint.setColor(dividerColor);
            paint.setStrokeWidth(dividerWidth);
            paint.setAntiAlias(true);

            canvas.drawLine(dividerBottomLeft, getHeight() - dividerWidth / 2, getWidth() - dividerBottomRight
                    , getHeight() - dividerWidth / 2, paint);
        }

        paint.reset();
        paint.setColor(titleColor);
        paint.setTextSize(titleSize);
        paint.setStrokeWidth(1 * density);
        paint.setAntiAlias(true);
        paint.getTextBounds(titleText, 0, titleText.length(), textRect);
        left = getPaddingLeft();
        top = getHeight() / 2 + textRect.height() / 2;
        canvas.drawText(titleText, left, top, paint);
        if (rightPic != null) {
//            final Rect pic = rightPic.getBounds();
          int  width= rightPic.getMinimumWidth();
            int height= rightPic.getMinimumHeight();
            left = getWidth() - width - getPaddingRight();
            top = getHeight() / 2 - height / 2;
            right = getWidth() - getPaddingRight();
            bottom = getHeight()/2 + height / 2;
            rightPic.setBounds(left, top,right,bottom);
            rightPic.draw(canvas);
//            canvas.save();

        }

    }
}
