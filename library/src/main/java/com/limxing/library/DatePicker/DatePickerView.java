package com.limxing.library.DatePicker;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.limxing.library.DatePicker.adapter.NumericWheelAdapter;
import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;

public class DatePickerView  {
	private WheelView year;
	private WheelView month;
	private WheelView day;
	
	private Context mContext;
	private DatePickerListener mListener;
	private Dialog dialog;

	public   DatePickerView(Context context,DatePickerListener listener){
		mContext=context;
		mListener=listener;
	}

	public void show(){
		 dialog = new Dialog(mContext, R.style.MMTheme_DatePicker);
		dialog.setCancelable(false);

		dialog.setContentView(getDataPick());
		Window w=dialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.dimAmount=0.4f;
		lp.gravity = Gravity.BOTTOM;
		lp.width = DisplayUtil.getScreenWith(mContext); //设置宽度
		w.setAttributes(lp);

		dialog.show();
	}


	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// ͨ��Calendar���������Ҫ+1
		int curDate = c.get(Calendar.DATE);


		View view = View.inflate(mContext,R.layout.wheel_date_picker, null);
		view.findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				mListener.finish();
			}
		});

		month = (WheelView) view.findViewById(R.id.month);
		year = (WheelView) view.findViewById(R.id.year);

		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
				mContext, 1950, norYear);
		numericWheelAdapter1.setLabel("年");
		numericWheelAdapter1.setTextGravity(Gravity.RIGHT);
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);// �Ƿ��ѭ������
		year.addScrollingListener(scrollListener);

		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
				mContext, 1, 12, "%d");
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(norYear, curMonth);
		day.setCyclic(true);
		day.addScrollingListener(scrollListener);

		year.setVisibleItems(5);// ������ʾ����
		month.setVisibleItems(5);
		day.setVisibleItems(5);

		year.setCurrentItem(norYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		return view;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mContext,
				1, getDay(arg1, arg2), "%d");
		numericWheelAdapter.setTextGravity(Gravity.LEFT);
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}
	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}
	/**
	 * ������������
	 */
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;// ��
			int n_month = month.getCurrentItem() + 1;// ��
			int n_day = day.getCurrentItem() + 1;// ��
			initDay(n_year, n_month);

			mListener.dateChange(n_year+"-"+n_month+"-"+n_day);
		}
	};


	public void setOnListener(DatePickerListener listener) {
		// TODO Auto-generated method stub
		this.mListener=listener;
		
	}
	
	public interface DatePickerListener{
		public void dateChange(String string);
		public void finish();
	}

}
