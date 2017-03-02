package me.leefeng.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.leefeng.library.R;

/**
 * Created by limxing on 2017/1/17.
 */

public class PlayItemView extends ImageView {
    private boolean isPlay;

    public PlayItemView(Context context) {
        super(context);
    }

    public PlayItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPlay) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.leefeng_icon_play);
            canvas.drawBitmap(bitmap, null, new Rect(0, 0, w, h), null);
        }
    }

    public void setPlay(boolean play) {
        isPlay = play;
        postInvalidate();
    }
}
