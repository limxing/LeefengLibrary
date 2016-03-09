package com.limxing.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 这是一个自定义的ImageView,宽度等于高度的图片,一般用于GrideView展示图片
 *
 */
public class MyImageView extends ImageView {

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyImageView(Context context) {
		super(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));
		int childWidthSize = getMeasuredWidth();

		int childHeightSize = getMeasuredHeight();
		// 高度和宽度一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
				childWidthSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setScaleType(ImageView.ScaleType.CENTER_CROP);
	}
}
