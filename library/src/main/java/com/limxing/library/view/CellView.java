package com.limxing.library.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limxing.library.R;
import com.limxing.library.utils.DisplayUtil;
import com.limxing.library.utils.ToastUtils;



/**
 * 版权：北京航天世景信息技术有限公司
 * <p/>
 * 作者：李利锋
 * <p/>
 * 创建日期：2016/4/24 15:02
 * <p/>
 * 描述：
 * <p/>
 * 修改历史：
 */
public class CellView extends RelativeLayout implements TextWatcher, View.OnClickListener {
    private static final float DEFALT_TEXT_SIZE = 30;
    private boolean topLine;
    private boolean isSendType;//设置edittext的按钮
    private int length;
    private boolean isPassword;
    private Drawable leftDrawable;
    private boolean clickAble = true;//在style下是否可以选择
    private boolean showRightPic = true;
    private String mTitle;
    private String mHint;
    private EditText editText;
    private int style;
    private boolean isHidden;//是否line充满屏幕
    private String result = "";
    private Context mContext;
    private String mSelect;
    private String[] selects;
    private TextView textView;
    private String netName;
    private int cellId;

    /**
     * 涉及到网络的cell 初始化
     *
     * @param context
     * @param title
     * @param hint
     * @param style
     * @param select
     * @param isHidden
     * @param netName
     */
    public CellView(Context context, String title, String hint, int style, String select, boolean isHidden, String netName) {
        super(context);
        mTitle = title;
        mHint = hint;
        this.style = style;
        mContext = context;
        mSelect = select;
        this.isHidden = isHidden;
        this.netName = netName;
        init(context);
    }

    /**
     * 不涉及到网络的cell
     *
     * @param context
     * @param title
     * @param hint
     * @param style
     * @param select
     * @param netName
     */
    public CellView(Context context, String title, String hint, int style, String select, String netName) {
        super(context);
        mTitle = title;
        mHint = hint;
        this.style = style;
        this.netName = netName;
        mContext = context;
        mSelect = select;
        init(context);
    }


    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CellView);
        mTitle = typedArray.getString(R.styleable.CellView_titleText);
        mHint = typedArray.getString(R.styleable.CellView_hintText);
        style = typedArray.getInt(R.styleable.CellView_titleStyle, 1);
        length = typedArray.getInt(R.styleable.CellView_length, 0);
        isHidden = typedArray.getBoolean(R.styleable.CellView_hiddenLine, false);
        showRightPic = typedArray.getBoolean(R.styleable.CellView_showRightPic, true);
        clickAble = typedArray.getBoolean(R.styleable.CellView_clickAble, true);
        isPassword = typedArray.getBoolean(R.styleable.CellView_isPassword, false);
        isSendType = typedArray.getBoolean(R.styleable.CellView_setSendType, false);
        mSelect = typedArray.getString(R.styleable.CellView_mSelect);
        leftDrawable = typedArray.getDrawable(R.styleable.CellView_drawable);
        netName = typedArray.getString(R.styleable.CellView_netName);
        topLine = typedArray.getBoolean(R.styleable.CellView_topLine, false);
        typedArray.recycle();
        init(context);

    }

    private void init(Context context) {
        int mHeight = DisplayUtil.dip2px(context, 60);
        setMinimumHeight(mHeight);
        setBackgroundColor(getResources().getColor(R.color.white));
        int padLeft = DisplayUtil.dip2px(context, 20);
        TextView tv = new TextView(context);
        tv.setText(mTitle);
        tv.setTextColor(getResources().getColor(R.color.cellview_color));
        //调节左边文字大小
        tv.setTextSize(DisplayUtil.px2sp(context, DEFALT_TEXT_SIZE));
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            tv.setCompoundDrawables(leftDrawable, null, null, null);
            tv.setCompoundDrawablePadding(10);
        }
        tv.measure(0, 0);
        int w = tv.getMeasuredWidth();
        int h = tv.getMeasuredHeight();
        int mt = (mHeight - h) / 2;
        MarginLayoutParams mp = new MarginLayoutParams(w, h);  //item的宽高
        mp.setMargins(padLeft, mt, 0, 0);//分别是margin_top那四个属性
        LayoutParams params = new LayoutParams(mp);
//        params.addRule(RelativeLayout.CENTER_VERTICAL);
        tv.setLayoutParams(params);
        addView(tv);

        View line = new View(context);
        line.setBackgroundColor(getResources().getColor(R.color.cell_line_color));
        h = DisplayUtil.dip2px(context, 1);
        MarginLayoutParams mpLine = new MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);  //item的宽高
        if (isHidden) {
            mpLine.setMargins(0, mHeight, 0, 0);//分别是margin_top那四个属性
        } else {
            mpLine.setMargins(padLeft, mHeight, 0, 0);//分别是margin_top那四个属性
        }
        LayoutParams mpLineparams = new LayoutParams(mpLine);
        line.setLayoutParams(mpLineparams);
        addView(line);

        if (topLine) {
            View topline = new View(context);
            topline.setBackgroundColor(getResources().getColor(R.color.cell_line_color));

            if (isHidden) {
                mpLine.setMargins(0, 0, 0, 0);//分别是margin_top那四个属性
            } else {
                mpLine.setMargins(padLeft, 0, 0, 0);//分别是margin_top那四个属性
            }
            LayoutParams topLineparams = new LayoutParams(mpLine);
            topline.setLayoutParams(topLineparams);
            addView(topline);
        }

        if (style == 2) {
            textView = new TextView(context);
            textView.setText(mHint);
            textView.setTextColor(getResources().getColor(R.color.cellview_edite_textcolor));
            //调节右边文字的大小
            textView.setTextSize(DisplayUtil.px2sp(context, DEFALT_TEXT_SIZE));
            if (clickAble) {
                textView.setOnClickListener(this);
            }
            textView.setGravity(Gravity.RIGHT);
            if (showRightPic) {
                Drawable rightDrawable = getResources().getDrawable(R.drawable.select_pic);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                textView.setCompoundDrawables(null, null, rightDrawable, null);
            }
            textView.measure(0, 0);
            w = DisplayUtil.getScreenWith(context) - w - 2 * padLeft;
            h = textView.getMeasuredHeight();
            MarginLayoutParams editTextmp = new MarginLayoutParams(w
                    , h);  //item的宽高
            editTextmp.setMargins(0, mt, padLeft, 0);//分别是margin_top那四个属性
            LayoutParams editeparams = new LayoutParams(editTextmp);
            editeparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

//        editeparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textView.setLayoutParams(editeparams);
            addView(textView);
        }
        if (style == 1) {
            editText = new EditText(context);
            editText.setBackground(null);
            editText.addTextChangedListener(this);
            editText.setHint(mHint);
            editText.setHintTextColor(getResources().getColor(R.color.cellview_edite_texthintcolor));
            editText.setTextColor(getResources().getColor(R.color.cellview_edite_textcolor));
            //调节右边输入框文字的大小
            editText.setTextSize(DisplayUtil.px2sp(context, DEFALT_TEXT_SIZE));
            editText.setGravity(Gravity.RIGHT);

            if (length > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
            }
//
            editText.setSingleLine(true);
            editText.measure(0, 0);
            w = DisplayUtil.getScreenWith(context) - w - 2 * padLeft;
            h = editText.getMeasuredHeight();
            MarginLayoutParams editTextmp = new MarginLayoutParams(w
                    , h);  //item的宽高
            editTextmp.setMargins(0, mt, padLeft, 0);//分别是margin_top那四个属性
            LayoutParams editeparams = new LayoutParams(editTextmp);
            editeparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        editeparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            editText.setLayoutParams(editeparams);
            if (isPassword) {
//                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
            if (isSendType) {
                editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
                editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            }
            addView(editText);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editText.getText().toString();
        if (text.equals(mHint)) {
            return;
        }
        result = text;
    }

    public String getResult() {
        return result;
    }

    /**
     * 点击选择
     *
     * @param view
     */
    @Override
    public void onClick(final View view) {
//            Method method = Data.class.getMethod(mSelect, String[].class);
//            final String[] cities = (String[]) method.invoke(null,-100001);

        if (!clickAble) {
            return;
        }
        selects = new String[0];
        /**
         * 这里应该给selects赋值,
         */
        if (selects.length == 0) {
            ToastUtils.showLong(mContext, "没有相关数据");
            return;
        }
//        BottomDialog.showAlert(mContext, mHint, selects, new BottomDialog.OnClickListener() {
//            @Override
//            public void onClick(int which) {
//                result = selects[which];
//                ((TextView) view).setText(result);
//            }
//        }, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//            }
//        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mHint);
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(selects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result = selects[which];
                ((TextView) view).setText(result);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public EditText getEditText() {

        return editText;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public String getNetName() {
        return netName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStyle() {
        return style;
    }

    public void setClickAble(boolean clickAble) {
        this.clickAble = clickAble;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }
}
