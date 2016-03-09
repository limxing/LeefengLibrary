package com.limxing.library.MyView;
/**
 * 这是一个滑动的,有标题的滑动
 *
 * 实际操作可能需要把viewpager单独在activity中操作
 */
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.library.R;

@SuppressLint("NewApi")
public class LLFViewPager extends RelativeLayout {
	private Context context;
	private ArrayList<TextView> mList;
	private float density;
	private int widthPad;
	private int width3;

	public LLFViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public LLFViewPager(Context context) {
		super(context);
		this.context = context;
		init();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		final RadioGroup viewpager_rg = (RadioGroup) findViewById(R.id.viewpager_rg);
		final ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
		final View viewpager_image = findViewById(R.id.viewpager_image);

		final MarginLayoutParams mlp = new MarginLayoutParams(
				viewpager_image.getLayoutParams());
		mlp.setMargins(widthPad, (int) (42 * density), 0, 0);

		ViewGroup.LayoutParams lp = new LayoutParams(mlp);
		viewpager_image.setLayoutParams(lp);
		for (int i = 0; i < mList.size(); i++) {
			RadioButton b = (RadioButton) viewpager_rg.getChildAt(i);
			final int position = i;
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewpager.setCurrentItem(position, true);

				}
			});
		}
		viewpager.setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				container.removeView(mList.get(position));
			}

			@Override
			public int getItemPosition(Object object) {
				// TODO Auto-generated method stub
				return super.getItemPosition(object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(mList.get(position));
				return mList.get(position);
			}
		});
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				RadioButton b;
				switch (arg0) {
				case 0:
					b = (RadioButton) findViewById(R.id.rb_0);
					b.setChecked(true);

					break;
				case 1:
					b = (RadioButton) findViewById(R.id.rb_1);
					b.setChecked(true);
					break;
				case 2:
					b = (RadioButton) findViewById(R.id.rb_2);
					b.setChecked(true);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if (arg1 != 0) {

					int s = widthPad + width3 * arg0 + arg2 / 3;

					mlp.setMargins(s, (int) (42 * density), 0, 0);
					ViewGroup.LayoutParams lp = new LayoutParams(
							mlp);
					viewpager_image.setLayoutParams(lp);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	private void init() {
		mList = new ArrayList<TextView>();
		for (int i = 0; i < 3; i++) {
			TextView view = new TextView(context);
			view.setText("这是界面：" + i);
			mList.add(view);
		}

		DisplayMetrics dm = getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		density = dm.density;
		width3 = width / 3;
		widthPad = (int) ((width3 - 70 * density) / 2);

		Log.i("haha:", dm.widthPixels + ":" + dm.density + ":" + dm.densityDpi);

	}

}
