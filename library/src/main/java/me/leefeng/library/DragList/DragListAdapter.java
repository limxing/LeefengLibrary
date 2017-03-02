package me.leefeng.library.DragList;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leefeng.library.R;

/***
 * 自定义可拖拽ListView适配器
 *
 * @author zihao
 *
 */
public class DragListAdapter extends BaseAdapter {
	private List<String> mDataList;// 标题数组
	private Context mContext;

	/**
	 * DragListAdapter构造方法
	 *
	 * @param context
	 *            // 上下文对象
	 * @param dataList
	 *            // 数据集合
	 */
	public DragListAdapter(Context context, ArrayList<String> dataList) {
		this.mContext = context;
		this.mDataList = dataList;
	}

	/**
	 * 设置是否显示下降的Item
	 *
	 * @param showItem
	 */
	public void showDropItem(boolean showItem) {
		this.mShowItem = showItem;
	}

	/**
	 * 设置不可见项的位置标记
	 *
	 * @param position
	 */
	public void setInvisiblePosition(int position) {
		mInvisilePosition = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/***
		 * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
		 * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
		 */
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.drag_list_item, null);

		initItemView(position, convertView);

		TextView titleTv = (TextView) convertView
				.findViewById(R.id.drag_item_title_tv);
		titleTv.setText(mDataList.get(position));

		if (isChanged) {// 判断是否发生了改变

			if (position == mInvisilePosition) {

				if (!mShowItem) {// 在拖拽过程不允许显示的状态下，设置Item内容隐藏

					// 因为item背景为白色，故而在这里要设置为全透明色防止有白色遮挡问题（向上拖拽）
					convertView.findViewById(R.id.drag_item_layout)
							.setBackgroundColor(0x0000000000);

					// 隐藏Item上面的内容
					int vis = View.INVISIBLE;
					convertView.findViewById(R.id.drag_item_image)
							.setVisibility(vis);
					convertView.findViewById(R.id.drag_item_close_layout)
							.setVisibility(vis);
					titleTv.setVisibility(vis);

				}

			}

			if (mLastFlag != -1) {

				if (mLastFlag == 1) {

					if (position > mInvisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, -mHeight);
						convertView.startAnimation(animation);
					}

				} else if (mLastFlag == 0) {

					if (position < mInvisilePosition) {
						Animation animation;
						animation = getFromSelfAnimation(0, mHeight);
						convertView.startAnimation(animation);
					}

				}

			}
		}

		return convertView;
	}

	/**
	 *
	 * 初始化Item视图
	 *
	 * @param convertView
	 */
	private void initItemView(final int position, final View convertView) {

		if (convertView != null) {
			// 设置对应的监听
			convertView.findViewById(R.id.drag_item_close_layout)
					.setOnClickListener(new OnClickListener() {// 删除

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							removeItem(position);
						}
					});
		}

	}

	private int mInvisilePosition = -1;// 用来标记不可见Item的位置
	private boolean isChanged = true;// 标识是否发生改变
	private boolean mShowItem = false;// 标识是否显示拖拽Item的内容

	/***
	 * 动态修改ListView的方位.
	 *
	 * @param startPosition
	 *            点击移动的position
	 * @param endPosition
	 *            松开时候的position
	 */
	public void exchange(int startPosition, int endPosition) {
		Object startObject = getItem(startPosition);

		if (startPosition < endPosition) {
			mDataList.add(endPosition + 1, (String) startObject);
			mDataList.remove(startPosition);
		} else {
			mDataList.add(endPosition, (String) startObject);
			mDataList.remove(startPosition + 1);
		}

		isChanged = true;
	}

	/**
	 * 动态修改Item内容
	 *
	 * @param startPosition
	 *            // 开始的位置
	 * @param endPosition
	 *            // 当前停留的位置
	 */
	public void exchangeCopy(int startPosition, int endPosition) {
		Object startObject = getCopyItem(startPosition);

		if (startPosition < endPosition) {// 向下移动
			mCopyList.add(endPosition + 1, (String) startObject);
			mCopyList.remove(startPosition);
		} else {// 向上拖动或者不动
			mCopyList.add(endPosition, (String) startObject);
			mCopyList.remove(startPosition + 1);
		}

		isChanged = true;
	}

	/**
	 * 删除指定的Item
	 *
	 * @param pos
	 *            // 要删除的下标
	 */
	private void removeItem(int pos) {
		if (mDataList != null && mDataList.size() > pos) {
			mDataList.remove(pos);
			this.notifyDataSetChanged();
		}
	}

	/**
	 * 获取镜像(拖拽)Item项
	 *
	 * @param position
	 * @return
	 */
	public Object getCopyItem(int position) {
		return mCopyList.get(position);
	}

	/**
	 * 获取Item总数
	 */
	@Override
	public int getCount() {
		return mDataList.size();
	}

	/**
	 * 获取ListView中Item项
	 */
	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 添加拖动项
	 *
	 * @param start
	 *            // 要进行添加的位置
	 * @param obj
	 */
	public void addDragItem(int start, Object obj) {
		mDataList.remove(start);// 删除该项
		mDataList.add(start, (String) obj);// 添加删除项
	}

	private ArrayList<String> mCopyList = new ArrayList<String>();

	public void copyList() {
		mCopyList.clear();
		for (String str : mDataList) {
			mCopyList.add(str);
		}
	}

	public void pastList() {
		mDataList.clear();
		for (String str : mCopyList) {
			mDataList.add(str);
		}
	}

	private boolean isSameDragDirection = true;// 是否为相同方向拖动的标记
	private int mLastFlag = -1;
	private int mHeight;
	private int mDragPosition = -1;

	/**
	 * 设置是否为相同方向拖动的标记
	 *
	 * @param value
	 */
	public void setIsSameDragDirection(boolean value) {
		isSameDragDirection = value;
	}

	/**
	 * 设置拖动方向标记
	 *
	 * @param flag
	 */
	public void setLastFlag(int flag) {
		mLastFlag = flag;
	}

	/**
	 * 设置高度
	 *
	 * @param value
	 */
	public void setHeight(int value) {
		mHeight = value;
	}

	/**
	 * 设置当前拖动位置
	 *
	 * @param position
	 */
	public void setCurrentDragPosition(int position) {
		mDragPosition = position;
	}

	/**
	 * 从自身出现的动画
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	private Animation getFromSelfAnimation(int x, int y) {
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, x,
				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, y);
		translateAnimation
				.setInterpolator(new AccelerateDecelerateInterpolator());
		translateAnimation.setFillAfter(true);
		translateAnimation.setDuration(100);
		translateAnimation.setInterpolator(new AccelerateInterpolator());
		return translateAnimation;
	}
}