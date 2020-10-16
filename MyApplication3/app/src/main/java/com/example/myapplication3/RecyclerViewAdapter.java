package com.example.myapplication3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * @创建时间 2020/10/16 9:55
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.HomeHolder> {
    private ArrayList<Bean.DataBean> beans;
    private Context context;

    public RecyclerViewAdapter(ArrayList<Bean.DataBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview, parent, false);

        return new HomeHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, final int position) {
        Glide.with(context).load(beans.get(position).getImagePath()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(holder.iv_image);
        holder.tv_desc.setText(beans.get(position).getDesc());
        holder.tv_title.setText(beans.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClick.OnLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_desc;
        TextView tv_title;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
    private OnItemClick onItemClick;
    private OnLongItemClick onLongClick;
    interface OnItemClick{
        void OnClick(int position);
    }
    interface OnLongItemClick{
        void OnLongClick(int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnLongClick(OnLongItemClick onLongClick) {
        this.onLongClick = onLongClick;
    }
}
