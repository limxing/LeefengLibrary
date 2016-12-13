package me.leefeng.imageselector.imagelook;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.leefeng.imageselector.Image;
import me.leefeng.imageselector.PhotoView.PhotoView;

/**
 * 版权：北京航天世景信息技术有限公司
 * <p>
 * 作者：李利锋
 * <p>
 * 创建日期：2016/8/30 15:23
 * <p>
 * 描述：
 * <p>
 * 修改历史：
 */
public class ViewPagerAdapter extends PagerAdapter {
    private  Context context;
    private List<Image> list;
    private List<WeakReference<PhotoView>> imageList;

    public ViewPagerAdapter(List list,Context context) {
        this.list = list;
        this.context=context;
        imageList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Image att = list.get(position);
        PhotoView view;
        if (imageList.isEmpty()) {
            initView(4);
        }
        view = imageList.get(0).get();

        if (view == null) {  //由于用了弱引用，所以当垃圾回收器进行回收的时候就回收所有内存
            clear();
            imageList.clear();   //需要先清理，GC后对象内存被回收
            initView(2);
            view = imageList.get(0).get();
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {   //这里的parent是ViewPager
        } else {
            container.addView(view);
        }
        view.enable();
        view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(container.getContext())
                .load(att.getPath())
                .into(view);

        imageList.remove(0);
        return view;
    }

    /**
     * @param num
     */
    public void initView(int num) {
        for (int i = 0; i < num; i++) {
            imageList.add(new WeakReference<>(new PhotoView(context)));
        }
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (imageList != null) {
            imageList.add(new WeakReference<>((PhotoView) object));   //添加进缓存
        }
        container.removeView((View) object);
        Glide.clear((View) object);
    }


    public Image getCurItem(int i) {
        return list.get(i);
    }

    public void onDestory() {
        context=null;
        list = null;
        clear();
        imageList.clear();
        imageList = null;
    }

    public void clear() {
        for (int i = 0; i < imageList.size(); i++) {
            if (imageList.get(i).get() != null) {
                Glide.clear(imageList.get(i).get());
            }
        }
    }

    public void setList(List<Image> attachments) {
        list = attachments;
        notifyDataSetChanged();
    }


    /**
     * 处理notifyDataSetChanged无反应
     */
    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
