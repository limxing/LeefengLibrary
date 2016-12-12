package me.leefeng.imageselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limxing on 2016/12/11.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListView> {
    private List<Image> list;
    private Context context;
    private List<Image> checkedList;
    private ImageListItemListener listItemListener;

    public ImageListAdapter(List<Image> imageList, Context context, ImageListItemListener listItemListener) {
        this.list = imageList;
        this.context = context;
        checkedList = new ArrayList<>();
        ImgSelConfig.checkedList = checkedList;//指向
        this.listItemListener = listItemListener;
    }

    @Override
    public ImageListView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageListView(View.inflate(parent.getContext(),
                R.layout.selimg_list_item, null));
    }

    @Override
    public void onBindViewHolder(final ImageListView holder, final int position) {
        final Image image = list.get(position);
        Glide.with(context).load(image.getPath()).placeholder(R.drawable.ic_default_image)
                .into(holder.imageView);
        if (ImgSelConfig.maxNum != 0) {
            if (checkedList.contains(image)) {
                holder.checkBox.setImageResource(R.drawable.imgsel_icon_selected);
            } else {
                holder.checkBox.setImageResource(R.drawable.imgsel_icon_unselected);
            }
            holder.checkBox.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (checkedList.contains(image)) {
                                checkedList.remove(image);
                                holder.checkBox.setImageResource(R.drawable.imgsel_icon_unselected);

                            } else if (checkedList.size() >= ImgSelConfig.maxNum) {
                                Toast.makeText(context, "最多选择" + ImgSelConfig.maxNum + "张图片", Toast.LENGTH_SHORT).show();
                            } else {
                                checkedList.add(image);
                                holder.checkBox.setImageResource(R.drawable.imgsel_icon_selected);
                            }

                            listItemListener.onItemChecked(position);
                        }
                    }

            );
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener()

                                            {
                                                @Override
                                                public void onClick(View view) {
                                                    listItemListener.onItemClick(position);
                                                }
                                            }

        );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void clearCheckList() {
        checkedList.clear();
    }

    public List<Image> getCheckList() {
        return checkedList;
    }

    public void destory() {
        context = null;
        listItemListener = null;
        checkedList.clear();
        checkedList = null;
        list = null;
    }

    public void addCheckedImage(Image image) {
        checkedList.add(image);
    }


    public class ImageListView extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView checkBox;

        public ImageListView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.selimg_list_item_iv);
            checkBox = (ImageView) itemView.findViewById(R.id.selimg_list_item_cb);
        }

    }

    /**
     * 返回可存储的数组
     *
     * @return
     */
    public ArrayList<String> getStringArray() {
        ArrayList<String> list = new ArrayList<>();
        for (Image image : checkedList) {
            list.add(image.getPath());
        }
        return list;
    }
}
