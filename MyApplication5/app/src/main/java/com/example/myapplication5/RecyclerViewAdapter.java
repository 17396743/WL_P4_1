package com.example.myapplication5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

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
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_recycler_view_adapter, parent, false);

        return new HomeHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, final int position) {
        Glide.with(context).load(beans.get(position).getImagePath()).into(holder.iv_image);
        holder.tv_dec.setText(beans.get(position).getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItenClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_dec;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_dec = (TextView) itemView.findViewById(R.id.tv_dec);
        }
    }

    private OnItenClick onItenClick;

    public void setOnItenClick(OnItenClick onItenClick) {
        this.onItenClick = onItenClick;
    }

    interface OnItenClick {
        void onClick(int position);
    }


}