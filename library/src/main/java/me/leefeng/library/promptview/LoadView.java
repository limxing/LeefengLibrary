package me.leefeng.library.promptview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import me.leefeng.library.R;

/**
 * Created by limxing on 16/1/7.
 */
@SuppressLint("AppCompatCustomView")
class LoadView extends ImageView {
    public static final int PROMPT_SUCCESS = 101;
    public static final int PROMPT_LOADING = 102;
    public static final int PROMPT_ERROR = 103;
    public static final int PROMPT_NONE = 104;
    public static final int PROMPT_INFO = 105;
    public static final int PROMPT_WARN = 106;
    public static final int PROMPT_ALERT_WARN = 107;
    private static final String TAG = "LOADVIEW";
    public static final int PROMPT_CUSTOM = 108;
    private PromptView promptView;
    private Builder builder;
    private int width;
    private int height;
    private ValueAnimator animator;
    private Paint paint;
    private float density;
    private Rect textRect;
    private int canvasWidth;
    private int canvasHeight;

    //    private float pad;
    private RectF roundRect;
//    private float round;

    private Drawable drawable;//loadingdrawable
    private int currentType;//当前窗口类型
    private PromptButton[] buttons = new PromptButton[]{};
    private RectF roundTouchRect;
    float buttonW;
    float buttonH;

    public LoadView(Context context) {
        super(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadView(Activity context, Builder builder, PromptView promptView) {
        super(context);
        this.builder = builder;
        this.promptView = promptView;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (canvasWidth == 0) {
            canvasWidth = getResources().getDisplayMetrics().widthPixels;
            canvasHeight = getResources().getDisplayMetrics().heightPixels;
        }
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(builder.backColor);
        paint.setAlpha(builder.backAlpha);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, canvasWidth / 2, canvasHeight / 2, paint);

        /**
         * 计算文字的宽度确定总宽
         */
//        TextPaint textPaint = new TextPaint();
//        textPaint.setColor(builder.textColor);
//        textPaint.setTextSize(density * builder.textSize);
//        textPaint.setAntiAlias(true);
//        textPaint.setStrokeWidth(1 * density);
//        StaticLayout layout = new StaticLayout(text, textPaint, (int) (canvasWidth * 0.8), Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);

        String text = builder.text;
        float pad = builder.padding * density;
        float round = builder.round * density;
        paint.reset();
        paint.setColor(builder.textColor);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * builder.textSize);
        paint.setAntiAlias(true);
        paint.getTextBounds(text, 0, text.length(), textRect);
        paint.getTextBounds(text, 0, text.length(), textRect);
        float popWidth = 0;
        float popHeight = 0;

        switch (currentType) {
            case PROMPT_ALERT_WARN:

                popWidth = Math.max(textRect.width() + pad * 2, 2 * buttonW);

                popHeight = textRect.height() + 3 * pad + height * 2 + buttonH;
                break;
            default:
                popWidth = Math.max(100 * density, textRect.width() + pad * 2);
                popHeight = textRect.height() + 3 * pad + height * 2;
                break;
        }


        float transTop = canvasHeight / 2 - height * 3 - pad;
        float transLeft = canvasWidth / 2 - popWidth / 2;

        canvas.translate(transLeft, transTop);


        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(builder.roundColor);
        paint.setAlpha(builder.roundAlpha);
        if (roundTouchRect == null)
            roundTouchRect = new RectF(transLeft, transTop, transLeft + popWidth, transTop + popHeight);
        if (roundRect == null)
            roundRect = new RectF(0, 0, popWidth, popHeight);
        roundRect.set(0, 0, popWidth, popHeight);
        canvas.drawRoundRect(roundRect, round, round, paint);


        paint.reset();
        paint.setColor(builder.textColor);
        paint.setStrokeWidth(1 * density);
        paint.setTextSize(density * builder.textSize);
        paint.setAntiAlias(true);

        float top = pad * 2 + height * 2 + textRect.height();
        float left = popWidth / 2 - textRect.width() / 2;

//        canvas.save();
//        canvas.translate(left, top);
//        layout.draw(canvas);
//        canvas.restore();//别忘了restore
        canvas.drawText(text, left, top, paint);


        if (currentType == PROMPT_ALERT_WARN) {
            top = top + pad;
            paint.setColor(Color.GRAY);
            paint.setStrokeWidth(1);
            paint.setAntiAlias(true);
            canvas.drawLine(0, top, popWidth, top, paint);
            if (buttons.length == 1) {
                PromptButton button = buttons[0];
                if (button.isFocus()) {
                    paint.reset();
                    paint.setAntiAlias(true);
                    paint.setColor(button.getFocusBacColor());
                    paint.setStyle(Paint.Style.FILL);

                    canvas.drawRect(0, top, popWidth, top + buttonH - round, paint);
                    canvas.drawCircle(round, top + buttonH - round, round, paint);
                    canvas.drawCircle(popWidth - round, top + buttonH - round, round, paint);
                    canvas.drawRect(round, top + buttonH - round, popWidth - round, top + buttonH, paint);

                }

                String buttonText = button.getText();
                paint.reset();
                paint.setColor(button.getTextColor());
                paint.setStrokeWidth(1 * density);
                paint.setTextSize(density * button.getTextSize());
                paint.setAntiAlias(true);
                paint.getTextBounds(buttonText, 0, buttonText.length(), textRect);

                button.setTouchRect(new RectF(transLeft, transTop + top,
                        transLeft + popWidth, transTop + top + buttonH));
                canvas.drawText(buttonText, popWidth / 2 - textRect.width() / 2,
                        top + textRect.height() / 2 + buttonH / 2, paint);
            }
            if (buttons.length > 1) {

                canvas.drawLine(popWidth / 2, top, popWidth / 2, popHeight, paint);
                for (int i = 0; i < buttons.length; i++) {
                    PromptButton button = buttons[i];
                    if (button.isFocus()) {
                        paint.reset();
                        paint.setAntiAlias(true);
                        paint.setColor(button.getFocusBacColor());
                        paint.setStyle(Paint.Style.FILL);
//                        paint.setAlpha(120);
                        canvas.drawRect(buttonW * i, top, buttonW * (i + 1), top + buttonH - round, paint);
                        if (i == 0) {
                            canvas.drawCircle(buttonW * i + round, top + buttonH - round, round, paint);
                            canvas.drawRect(round, top + buttonH - round, buttonW * (i + 1), top + buttonH, paint);
                        } else if (i == 1) {
                            canvas.drawCircle(buttonW * 2 - round, top + buttonH - round, round, paint);
                            canvas.drawRect(buttonW, top + buttonH - round, buttonW * 2 - round, top + buttonH, paint);
                        }
                    }
                    String buttonText = button.getText();
                    paint.reset();
                    paint.setColor(button.getTextColor());
                    paint.setStrokeWidth(1 * density);
                    paint.setTextSize(density * button.getTextSize());
                    paint.setAntiAlias(true);
                    paint.getTextBounds(buttonText, 0, buttonText.length(), textRect);

                    button.setTouchRect(new RectF(transLeft + i * buttonW, transTop + top,
                            transLeft + i * buttonW + buttonW, transTop + top + buttonH));
                    canvas.drawText(buttonText, buttonW / 2 - textRect.width() / 2 + i * buttonW,
                            top + textRect.height() / 2 + buttonH / 2, paint);

                }

            }

        }
        canvas.translate(popWidth / 2 - width, pad);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setScaleType(ScaleType.MATRIX);
        paint = new Paint();
        initData();
    }

    private void initData() {
        textRect = new Rect();
        density = getResources().getDisplayMetrics().density;

        buttonW = density * 120;
        buttonH = density * 44;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.cancel();
        animator = null;
        textRect = null;
        drawable = null;
        roundTouchRect = null;
        promptView.onDetach();
//        promptView = null;

    }


    private Matrix max;

    /**
     * loading start
     */
    private void start() {
        if (max == null || animator == null) {
            max = new Matrix();
            animator = ValueAnimator.ofInt(0, 12);
            animator.setDuration(12 * 80);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(Integer.MAX_VALUE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float degrees = 30 * (Integer) valueAnimator.getAnimatedValue();
                    max.setRotate(degrees, width, height);
                    setImageMatrix(max);
                }
            });
        }
        if (!animator.isRunning())
            animator.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (currentType == PROMPT_ALERT_WARN) {
            if (builder.cancleAble && event.getAction() == MotionEvent.ACTION_DOWN && !roundTouchRect.contains(x, y)) {
                promptView.dismiss();
            }
            for (PromptButton button : buttons) {
                if (button.getRect().contains(x, y)) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        button.setFocus(true);
                        invalidate();
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        button.setFocus(false);
                        invalidate();
                        if (button.getListener() != null)
                            button.getListener().onClick(button);
                        promptView.dismiss();
                    }
                    return true;
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                for (PromptButton button : buttons) {
                    button.setFocus(false);
                    invalidate();
                }
            }
        }
        return !builder.touchAble;
    }


    /**
     * 停止旋转
     */
    private void endAnimator() {
        if (animator != null && animator.isRunning()) {
            animator.end();
            max.setRotate(0, width, height);
            setImageMatrix(max);
        }
    }

    /**
     *
     */
    public void showLoading() {
        setImageDrawable(getResources().getDrawable(builder.icon));
        width = getDrawable().getMinimumWidth() / 2;
        height = getDrawable().getMinimumHeight() / 2;
        start();
        currentType = PROMPT_LOADING;

    }

    Builder getBuilder() {
        return builder;
    }

    public void showSomthing(int currentType) {
        endAnimator();
//        int drawableId = R.drawable.ic_prompt_success;
//        switch (currentType) {
//            case PROMPT_SUCCESS:
//                drawableId = R.drawable.ic_prompt_success;
//                break;
//            case PROMPT_ERROR:
//                drawableId = R.drawable.ic_prompt_error;
//                break;
//            case PROMPT_INFO:
//                drawableId = R.drawable.ic_prompt_info;
//                break;
//            case PROMPT_WARN:
//                drawableId = R.drawable.ic_prompt_warn;
//                break;
//            case PROMPT_ALERT_WARN:
//                drawableId = R.drawable.ic_prompt_alert_warn;
//                break;
//        }
        setImageDrawable(getResources().getDrawable(builder.icon));
        width = getDrawable().getMinimumWidth() / 2;
        height = getDrawable().getMinimumHeight() / 2;
        this.currentType = currentType;
        invalidate();
    }


    public void showSomthingAlert(PromptButton... button) {
        this.buttons = button;
        showSomthing(PROMPT_ALERT_WARN);

    }


//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        Log.i(TAG, "onKey: ");
//        return false;
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.i(TAG, "onKeyDown: ");
//        return super.onKeyDown(keyCode, event);
//    }

    //
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent: ");
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && currentType == PROMPT_ALERT_WARN) {
            promptView.dismiss();

            return false;
        }
//
        return super.dispatchKeyEvent(event);
    }

    public void setBuilder(Builder builder) {
        if (this.builder != builder)
            this.builder = builder;
    }
}