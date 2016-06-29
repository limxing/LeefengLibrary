package com.limxing.app.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by limxing on 16/6/29.
 */
public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.MasonryView> {
    public LoginAdapter() {

    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView text = new TextView(parent.getContext());
        text.setText("nijiaoshenme:" + viewType);
        return new MasonryView(text);
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
    holder.textView.setText("shenmeren"+position);
        holder.textView.setPadding(0,20*position,0,0);

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class MasonryView extends RecyclerView.ViewHolder {

//        ImageView imageView;
        TextView textView;

        public MasonryView(View itemView) {
            super(itemView);
//            imageView= (ImageView) itemView.findViewById(R.id.masonry_item_img );
            textView= (TextView) itemView;
        }

    }

}



