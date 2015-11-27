package com.limxing.library.PullToRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;

/**
 *
 <com.drayge.widgets.SwipeRefreshLayout
 android:id="@+id/main_fresh"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:layout_below="@+id/main_title"
 swiperefresh:srlAnimationStyle="rotate"
 swiperefresh:srlTextSize="16sp" >

 <ListView
 android:id="@+id/main_listview"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:background="#ffffff"
 android:dividerHeight="1dp" >
 </ListView>
 </com.drayge.widgets.SwipeRefreshLayout>
 */
public class PullToRefresh extends ViewGroup {

	private Context mContext;
	private int viewHeight;
	public PullToRefresh(Context context) {
		this(context, null);
	}

	public PullToRefresh(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context;
		LayoutInflater inflater = LayoutInflater.from(mContext);  
		View view = inflater.inflate(R.layout.edge_view, null);
		addView(view);
		TextView tv = (TextView) view.findViewById(R.id.pull_to_refresh_loadmore_text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		viewHeight = (DisplayUtil.getFontHeight(tv.getTextSize()) + view.getPaddingTop() + view.getPaddingBottom()) << 1;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();

		if (getChildCount() == 0) {
			return;
		}

		final View child = getChildAt(1);
		final int childLeft = getPaddingLeft();
		final int childTop = getPaddingTop() + viewHeight;
		final int childWidth = width - getPaddingLeft() - getPaddingRight();
		final int childHeight = height - viewHeight - getPaddingTop() - getPaddingBottom();
		child.layout(childLeft, childTop, childLeft + childWidth, childTop
				+ childHeight);
		
		
		final View child2 = getChildAt(0);
		child2.layout(childLeft, getPaddingTop(), r, viewHeight);
		
		//test();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getChildCount() > 0) {
			getChildAt(1).measure(
					MeasureSpec.makeMeasureSpec(getMeasuredWidth()
							- getPaddingLeft() - getPaddingRight(),
							MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(getMeasuredHeight() - viewHeight
							- getPaddingTop() - getPaddingBottom(),
							MeasureSpec.EXACTLY));
			
			getChildAt(0).measure(
					MeasureSpec.makeMeasureSpec(getMeasuredWidth()
							- getPaddingLeft() - getPaddingRight(),
							MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(getPaddingTop() + viewHeight,
							MeasureSpec.EXACTLY));
		}
	}

	
	private void test() {
		this.offsetTopAndBottom(-60);	
	}
}
