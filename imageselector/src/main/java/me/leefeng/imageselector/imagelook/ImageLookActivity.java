package me.leefeng.imageselector.imagelook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.imageselector.Image;
import me.leefeng.imageselector.ImgSelConfig;
import me.leefeng.imageselector.R;
import me.leefeng.imageselector.StatusBarCompat;

/**
 * Created by limxing on 2016/12/12.
 */

public class ImageLookActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, CompoundButton.OnCheckedChangeListener {
    private List<Image> list;
    private ViewPagerAdapter adapter;
    private ViewPager vp;
    private TextView title_name;
    private ImageView imageLookCheck;
//    private CheckBox imagelook_cb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_imagelook);
        vp = (ViewPager) findViewById(R.id.imagelook_vp);
        title_name = (TextView) findViewById(R.id.selectimage_title_name);
        imageLookCheck = (ImageView) findViewById(R.id.imagelook_cb);
        imageLookCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = vp.getCurrentItem();
                if (ImgSelConfig.checkedList.contains(list.get(position))) {
                    ImgSelConfig.checkedList.remove(list.get(position));
                    imageLookCheck.setImageResource(R.drawable.imgsel_icon_unselected);
                } else if (ImgSelConfig.checkedList.size() >= ImgSelConfig.maxNum) {
                    Toast.makeText(view.getContext(), "最多选择" + ImgSelConfig.maxNum + "张图片", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ImgSelConfig.checkedList.add(list.get(position));
                    imageLookCheck.setImageResource(R.drawable.imgsel_icon_selected);
                }
            }
        });
        int position = getIntent().getIntExtra("position", 0);
        list = ImgSelConfig.currentList;
        title_name.setText(position + 1 + "/" + list.size());
        adapter = new ViewPagerAdapter(list, this);
        vp.setAdapter(adapter);
        vp.setCurrentItem(position);
        if (ImgSelConfig.checkedList.contains(list.get(position))) {
            imageLookCheck.setImageResource(R.drawable.imgsel_icon_selected);
        }
        findViewById(R.id.imagelook_bac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        vp.addOnPageChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.onDestory();
        list = null;
    }

    /**
     * ViewPager监听事件
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        title_name.setText(position + 1 + "/" + list.size());
        if (ImgSelConfig.checkedList.contains(list.get(position))) {
            imageLookCheck.setImageResource(R.drawable.imgsel_icon_selected);
        } else {
            imageLookCheck.setImageResource(R.drawable.imgsel_icon_unselected);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Checkbox状态改变
     *
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            if (ImgSelConfig.checkedList.size() >= ImgSelConfig.maxNum) {
                Toast.makeText(this, "最多选择" + ImgSelConfig.maxNum + "张图片", Toast.LENGTH_SHORT).show();
//                compoundButton.setChecked(false);
//                imagelook_cb.setChecked(false);
                return;
            }
            if (!ImgSelConfig.checkedList.contains(list.get(vp.getCurrentItem())))
                ImgSelConfig.checkedList.add(list.get(vp.getCurrentItem()));
        } else {
            ImgSelConfig.checkedList.remove(list.get(vp.getCurrentItem()));
        }

    }
}
