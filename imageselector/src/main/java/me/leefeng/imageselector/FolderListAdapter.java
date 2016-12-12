package me.leefeng.imageselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by limxing on 2016/12/12.
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.FolderView> {
    private Context context;
    private List<Folder> list;
    private FolderListItemListener listItemListener;
    private int checkPosition;

    public FolderListAdapter(List<Folder> folderList, Context context, FolderListItemListener listItemListener) {
        this.list = folderList;
        this.context = context;
        this.listItemListener = listItemListener;
    }

    @Override
    public FolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FolderView(View.inflate(parent.getContext(), R.layout.folder_list_item, null));
    }

    @Override
    public void onBindViewHolder(final FolderView holder, final int position) {
        List<Image> l = list.get(position).images;
        Glide.with(context).load(l.get(0).getPath()).into(holder.image);
        holder.name.setText(list.get(position).name);
        holder.size.setText("(" + l.size() + ")");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listItemListener != null) {
                    listItemListener.folderListItemClick(position);
                    notifyItemChanged(checkPosition);
                    notifyItemChanged(position);
                    checkPosition = position;
                }
            }
        });
        if (position == checkPosition) {
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void destory() {
        context=null;
        listItemListener=null;
        list=null;
    }

    public class FolderView extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView size;
        View check;
        View view;

        public FolderView(View itemView) {
            super(itemView);
            view = itemView;
            image = (ImageView) itemView.findViewById(R.id.folder_item_image);
            name = (TextView) itemView.findViewById(R.id.folder_item_name);
            size = (TextView) itemView.findViewById(R.id.folder_item_size);
            check = itemView.findViewById(R.id.folder_item_check);
        }
    }


}
