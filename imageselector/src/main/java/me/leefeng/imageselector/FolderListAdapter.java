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

    public FolderListAdapter(List<Folder> folderList, Context context) {
        this.list = folderList;
        this.context = context;
    }

    @Override
    public FolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FolderView(View.inflate(parent.getContext(), R.layout.folder_list_item, null));
    }

    @Override
    public void onBindViewHolder(FolderView holder, int position) {
        List<Image> l = list.get(position).images;
        Glide.with(context).load(l.get(0).getPath()).into(holder.image);
        holder.name.setText(list.get(position).name);
        holder.size.setText(l.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FolderView extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView size;
        View check;

        public FolderView(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.folder_item_image);
            name = (TextView) itemView.findViewById(R.id.folder_item_name);
            size = (TextView) itemView.findViewById(R.id.folder_item_size);
            check = itemView.findViewById(R.id.folder_item_check);
        }
    }
}
