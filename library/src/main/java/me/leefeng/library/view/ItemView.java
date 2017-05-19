package me.leefeng.library.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.leefeng.library.R;


/**
 * Created by limxing on 2016/12/14.
 */

public class ItemView extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private float density;
    private String mTitle;
    private String mHint;
    private int tileStyle;
    private int editeLength;
    private int editeIputType;
    private int mHeight;
    private int leftDrawablePad;
    private float titleTextSize;
    private float valueTextSize;
    private boolean showRightPic;
    private boolean clickAble;
    private boolean isPassword;
    private boolean isSendType;
    private Drawable leftDrawable;
    private Drawable rightDrawable;
    private String netName;
    private boolean topLine;
    private boolean cancleAble;
    private String[] selects;
    private int padingLeft;
    private int titleColor;
    private int valueColor;
    private TextView valueTextView;
    private EditText editText;
    private int valueHintColor;
    private String result = "";
    private int lineColor;
    private boolean topLineBrim;
    private boolean bottomLineBrim;
    private int itemBacColor;
    private boolean metalDialog;
    private String cancleText;
    private SwitchView switchView;
    private int rightDrawablePad;
    private int padingRight;

    public ItemView(Context context) {
        super(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        density = getResources().getDisplayMetrics().density;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemView);
        mTitle = typedArray.getString(R.styleable.ItemView_titleText);
        mHint = typedArray.getString(R.styleable.ItemView_hintText);
        tileStyle = typedArray.getInt(R.styleable.ItemView_titleStyle, 1);
        editeLength = typedArray.getInt(R.styleable.ItemView_length, 0);
        editeIputType = typedArray.getInt(R.styleable.ItemView_inputType, InputType.TYPE_CLASS_TEXT);

        mHeight = (int) typedArray.getDimension(R.styleable.ItemView_cellHeight, 50 * density);
        leftDrawablePad = (int) typedArray.getDimension(R.styleable.ItemView_drawablePad, 5 * density);
        rightDrawablePad = (int) typedArray.getDimension(R.styleable.ItemView_rightDrawablePad, 5 * density);
        padingLeft = (int) typedArray.getDimension(R.styleable.ItemView_padingLeft, 20 * density);
        padingRight = (int) typedArray.getDimension(R.styleable.ItemView_padingRight, 20 * density);
        titleTextSize = typedArray.getDimension(R.styleable.ItemView_titleTextSize, 18 * density);
        valueTextSize = typedArray.getDimension(R.styleable.ItemView_valueTextSize, 16 * density);

        showRightPic = typedArray.getBoolean(R.styleable.ItemView_showRightPic, true);
        clickAble = typedArray.getBoolean(R.styleable.ItemView_clickAble, false);
        isPassword = typedArray.getBoolean(R.styleable.ItemView_isPassword, false);
        isSendType = typedArray.getBoolean(R.styleable.ItemView_setSendType, false);
        leftDrawable = typedArray.getDrawable(R.styleable.ItemView_drawable);
        rightDrawable = typedArray.getDrawable(R.styleable.ItemView_rightDrawable);
        netName = typedArray.getString(R.styleable.ItemView_netName);
        topLine = typedArray.getBoolean(R.styleable.ItemView_topLine, false);
        topLineBrim = typedArray.getBoolean(R.styleable.ItemView_topLineBrim, true);
        bottomLineBrim = typedArray.getBoolean(R.styleable.ItemView_bottomLineBrim, true);
        cancleAble = typedArray.getBoolean(R.styleable.ItemView_cancleAble, false);
        cancleText = typedArray.getString(R.styleable.ItemView_cancleText);
        metalDialog = typedArray.getBoolean(R.styleable.ItemView_metalDialog, false);
        String select = typedArray.getString(R.styleable.ItemView_mSelect);
        titleColor = typedArray.getColor(R.styleable.ItemView_titleColor, getResources().getColor(R.color.cellview_color));
        valueColor = typedArray.getColor(R.styleable.ItemView_valueColor, getResources().getColor(R.color.cellview_edite_textcolor));
        valueHintColor = typedArray.getColor(R.styleable.ItemView_valueHintColor, getResources().getColor(R.color.cellview_edite_texthintcolor));
        lineColor = typedArray.getColor(R.styleable.ItemView_lineColor, getResources().getColor(R.color.cell_line_color));
        itemBacColor = typedArray.getResourceId(R.styleable.ItemView_itemBacColor, R.drawable.itemview_bg_selector);
        typedArray.recycle();
        init(context);
        if (select != null) {
            selects = select.split(",");
        }

    }

    private void init(Context context) {

        setFocusable(true);
        setClickable(clickAble);
        setBackgroundResource(itemBacColor);
        View view = View.inflate(context, R.layout.itemview, null);
        addView(view);
        RelativeLayout itemview_container = (RelativeLayout) findViewById(R.id.itemview_container);
        LayoutParams layoutParams = (LayoutParams) itemview_container.getLayoutParams();
        layoutParams.height = mHeight;
        itemview_container.setLayoutParams(layoutParams);

        TextView textView = (TextView) view.findViewById(R.id.itemview_title);
        textView.setText(mTitle);

        textView.setTextColor(titleColor);
        textView.setTextSize(titleTextSize / density);
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
            textView.setCompoundDrawables(leftDrawable, null, null, null);
            textView.setCompoundDrawablePadding(leftDrawablePad);
        }
        textView.setPadding(padingLeft, 0, 0, 0);
        if (tileStyle == 2) {
            valueTextView = (TextView) view.findViewById(R.id.itemview_value_tv);
            valueTextView.setVisibility(View.VISIBLE);
            valueTextView.setText(mHint);
            valueTextView.setTextColor(valueColor);
            valueTextView.setTextSize(valueTextSize / density);
            valueTextView.setPadding(0, 0, padingRight, 0);
            if (clickAble) {
                valueTextView.setOnClickListener(this);
            }
            if (showRightPic) {
                if (rightDrawable == null) {
                    rightDrawable = getResources().getDrawable(R.drawable.select_pic);
                }
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                valueTextView.setCompoundDrawables(null, null, rightDrawable, null);
                valueTextView.setCompoundDrawablePadding(rightDrawablePad);
            }
        }
        if (tileStyle == 1) {
            editText = (EditText) view.findViewById(R.id.itemview_value_et);
            editText.setVisibility(View.VISIBLE);
            editText.setBackground(null);
            editText.setHint(mHint);
            editText.setHintTextColor(valueHintColor);
            editText.setTextColor(valueColor);
            editText.setTextSize(valueTextSize / density);
            editText.setPadding(0, 0, padingLeft, 0);
            if (editeLength > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(editeLength)});
            }
            editText.setInputType(editeIputType);
            if (isPassword) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            if (isSendType) {
                editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
            }
            if (clickAble) {
                editText.setOnClickListener(this);
                editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            onClick(view);
                        }
                    }
                });
            }
            editText.addTextChangedListener(this);
        }
        if (tileStyle == 3) {
            switchView = (SwitchView) view.findViewById(R.id.itemview_value_sw);
            switchView.setVisibility(View.VISIBLE);
        }
        View topLineView = view.findViewById(R.id.item_topline);
        View bottomLineView = view.findViewById(R.id.item_bottomline);
        if (topLine) {
            topLineView.setVisibility(View.VISIBLE);
            topLineView.setBackgroundColor(lineColor);
            if (!topLineBrim) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) topLineView.getLayoutParams();
                lp.setMargins(padingLeft, 0, 0, 0);
                topLineView.setLayoutParams(lp);
            }
        }
        bottomLineView.setBackgroundColor(lineColor);
        if (!bottomLineBrim) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottomLineView.getLayoutParams();
            lp.setMargins(padingLeft, 0, 0, 0);
            bottomLineView.setLayoutParams(lp);
        }

    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View view) {
        if (!clickAble) {
            return;
        }
        if (selects == null)
            selects = new String[0];
        /**
         */
        if (selects.length == 0) {
            Toast.makeText(view.getContext(), "没有相关数据", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder;
        if (metalDialog) {
            builder = new AlertDialog.Builder(view.getContext());
        } else {
            builder = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_HOLO_LIGHT);
        }


        if (mHint == null || mHint.length() == 0) {
            builder.setTitle(R.string.please_select);
        } else {
            builder.setTitle(mHint);
        }
        builder.setItems(selects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result = selects[which];
                if (tileStyle == 1) {
                    editText.setText(result);
                    editText.setSelection(result.length());
                } else {
                    valueTextView.setText(result);
                }
                dialog.dismiss();
            }
        });
        if (cancleAble) {
            builder.setNegativeButton(cancleText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editText.setText("");
                }
            });
        }
        builder.show();
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

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getValueTextView() {
        return valueTextView;
    }


    public void setEnable(boolean b) {
        this.clickAble = b;
        if (editText != null) {
            editText.setEnabled(b);
        }

    }

    /**
     * 设置开关的监听
     * @param listener
     */
    public void setSwitchChangeListener(SwitchView.OnStateChangedListener listener) {
        switchView.setOnStateChangedListener(listener);
    }

    public SwitchView getSwitchView() {
        return switchView;
    }
}
