package me.leefeng.library.LoopView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;



import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import me.leefeng.library.R;

/**

 * Created by Weidongjian on 2015/8/18.
 */
// * LoopView loopView = (LoopView) findViewById(R.id.loopview);
// * ArrayList<String> list = new ArrayList<>();
// * for (int i = 0; i < 15; i++) {
// * list.add(2000 + i+"");
// * }
// * //loopView.setNotLoop();
// * loopView.setListener(new OnItemSelectedListener() {
// *
// * @Override public void onItemSelected(int index) {
// * Log.d("debug", "Item " + index);
// * }
// * });
// * loopView.setItems(list);
// * loopView.setInitPosition(5);
// * //loopView.setTextSize(20);
public class LoopView extends View {

    //    private static final float CENTERCONTENTOFFSET = 6;
    private float scaleX = 1.05F;
    private int widthMeasureSpec;
    private float itemHeight;
//    private float centerY;

    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    Context context;

    Handler handler;
    private GestureDetector gestureDetector;
    OnItemSelectedListener onItemSelectedListener;

    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    Paint paintOuterText;
    Paint paintCenterText;
    Paint paintIndicator;

    List<String> items;

    float textSize;
    int maxTextWidth;
    int maxTextHeight;

    int colorGray;
    int colorBlack;
    int colorLightGray;

    float lineSpacingMultiplier;
    boolean isLoop;

    float firstLineY;
    float secondLineY;

    int totalScrollY;
    int initPosition;
    private int selectedItem;
    int preCurrentIndex;
    int change;

    int itemsVisible;

    int measuredHeight;
    int measuredWidth;
    int paddingLeft = 0;
    int paddingRight = 0;
    int halfCircumference;
    int radius;

    private int mOffset = 0;
    private float previousY;
    long startTime = 0;

    private Rect tempRect = new Rect();

    public LoopView(Context context) {
        super(context);

    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context,attributeset);
    }


    private void initLoopView(Context context, AttributeSet attributeset) {
        this.context = context;
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        gestureDetector.setIsLongpressEnabled(false);
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeset, R.styleable.LoopView);
        lineSpacingMultiplier=typedArray.getDimension(R.styleable.LoopView_lineSpacingMultiplier,1.6f);
        textSize=typedArray.getDimension(R.styleable.LoopView_textSize,20f);
        itemsVisible=typedArray.getInt(R.styleable.LoopView_itemsVisible,9);
        isLoop=typedArray.getBoolean(R.styleable.LoopView_isLoop,false);
        typedArray.recycle();
        colorGray = 0xffafafaf;
        colorBlack = 0xff313131;
        colorLightGray = 0xffc5c5c5;
        totalScrollY = 0;
        initPosition = -1;

        initPaints();

        setTextSize(textSize);
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(colorGray);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(Typeface.MONOSPACE);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint();
        paintCenterText.setColor(colorBlack);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(scaleX);
        paintCenterText.setTypeface(Typeface.MONOSPACE);
        paintCenterText.setTextSize(textSize);

        paintIndicator = new Paint();
        paintIndicator.setColor(colorLightGray);
        paintIndicator.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void remeasure() {
        if (items == null) {
            return;
        }

        measureTextWidthHeight();
        halfCircumference = (int) (itemHeight * (itemsVisible - 1));
//        halfCircumference = (int) (maxTextHeight * lineSpacingMultiplier * (itemsVisible - 1));
        measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
        radius = (int) (halfCircumference / Math.PI);
//        measuredWidth = maxTextWidth + paddingLeft + paddingRight;
        measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        firstLineY = (measuredHeight - itemHeight) / 2.0F;
        secondLineY = (measuredHeight + itemHeight) / 2.0F;
//        centerY = (measuredHeight + maxTextHeight) / 2.0F - CENTERCONTENTOFFSET;

//        firstLineY = (int) ((measuredHeight - lineSpacingMultiplier * maxTextHeight) / 2.0F);
//        secondLineY = (int) ((measuredHeight + lineSpacingMultiplier * maxTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        preCurrentIndex = initPosition;
    }

    private void measureTextWidthHeight() {
        for (int i = 0; i < items.size(); i++) {
            String s1 = items.get(i);
            paintCenterText.getTextBounds(s1, 0, s1.length(), tempRect);
            int textWidth = tempRect.width();
            if (textWidth > maxTextWidth) {
                maxTextWidth = (int) (textWidth * scaleX);
            }
            paintCenterText.getTextBounds("\u661F\u671F", 0, 2, tempRect); //
            int textHeight = tempRect.height();
            if (textHeight > maxTextHeight) {
                maxTextHeight = textHeight;
            }
        }
        itemHeight = lineSpacingMultiplier * maxTextHeight;

    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
//            float itemHeight = lineSpacingMultiplier * maxTextHeight;
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

//    void smoothScroll() {
//        int offset = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
//        cancelFuture();
//        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, offset), 0, 10, TimeUnit.MILLISECONDS);
//    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, velocityFling, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    public final void setNotLoop() {
        isLoop = false;
    }

    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.initPosition = 0;
        } else {
            if (items != null && (items.size() - 1) > initPosition) {
                this.initPosition = initPosition;
            }
        }
    }

    public final void setListener(OnItemSelectedListener OnItemSelectedListener) {
        onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setItems(List<String> items) {
        this.items = items;
        remeasure();
        invalidate();
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public int getPaddingLeft() {
        return paddingLeft;
    }

    @Override
    public int getPaddingRight() {
        return paddingRight;
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingRight = right;
    }

    public final int getSelectedItem() {
        return selectedItem;
    }
//
//    protected final void scrollBy(float velocityY) {
//        Timer timer = new Timer();
//        mTimer = timer;
//        timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
//    }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (items == null) {
            return;
        }

        String as[] = new String[itemsVisible];
        change = (int) (totalScrollY / (lineSpacingMultiplier * maxTextHeight));
        preCurrentIndex = initPosition + change % (items.size());

        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }

        int j2 = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
        int k1 = 0;
        while (k1 < itemsVisible) {
            int l1 = preCurrentIndex - (itemsVisible / 2 - k1);
            if (isLoop) {
                while (l1 < 0) {
                    l1 = l1 + items.size();
                }
                while (l1 > items.size() - 1) {
                    l1 = l1 - items.size();
                }
                as[k1] = items.get(l1);
            } else if (l1 < 0) {
                as[k1] = "";
            } else if (l1 > items.size() - 1) {
                as[k1] = "";
            } else {
                as[k1] = items.get(l1);
            }
            k1++;
        }
        canvas.drawLine(0.0F, firstLineY, measuredWidth, firstLineY, paintIndicator);
        canvas.drawLine(0.0F, secondLineY, measuredWidth, secondLineY, paintIndicator);

        int j1 = 0;
        while (j1 < itemsVisible) {
            canvas.save();
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * j1 - j2) * Math.PI) / halfCircumference;
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.drawText(as[j1], getTextX(as[j1], paintOuterText, tempRect), maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], getTextX(as[j1], paintCenterText, tempRect), maxTextHeight, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.drawText(as[j1], getTextX(as[j1], paintCenterText, tempRect), maxTextHeight, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], getTextX(as[j1], paintOuterText, tempRect), maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], getTextX(as[j1], paintCenterText, tempRect), maxTextHeight, paintCenterText);
                    selectedItem = items.indexOf(as[j1]);
                } else {
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], getTextX(as[j1], paintOuterText, tempRect), maxTextHeight, paintOuterText);
                }
                canvas.restore();
            }
            j1++;
        }
    }

    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        int textWidth = rect.width();
        textWidth *= scaleX;
        return (measuredWidth - textWidth) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        float itemHeight = lineSpacingMultiplier * maxTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();

                totalScrollY = (int) (totalScrollY + dy);

//                if (!isLoop) {
//                    float top = -initPosition * itemHeight;
//                    float bottom = (items.size() - 1 - initPosition) * itemHeight;
//
//                    if (totalScrollY < top) {
//                        totalScrollY = (int) top;
//                    } else if (totalScrollY > bottom) {
//                        totalScrollY = (int) bottom;
//                    }
//                }
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;
                    if(totalScrollY - itemHeight*0.3 < top){
                        top = totalScrollY - dy;
                    }
                    else if(totalScrollY + itemHeight*0.3 > bottom){
                        bottom = totalScrollY - dy;
                    }

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset);

//                    if ((System.currentTimeMillis() - startTime) > 120) {
                        // 处理拖拽事件
                        smoothScroll(ACTION.DAGGLE);
//                    } else {
                        // 处理条目点击事件
//                        smoothScroll(ACTION.CLICK);
//                    }
                }
                break;
        }

        invalidate();
        return true;
    }
}
