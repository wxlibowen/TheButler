package com.example.administrator.my_first_demo.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.WeChatData;
import com.example.administrator.my_first_demo.ui.WebViewActivity;
import com.example.administrator.my_first_demo.utils.L;

import java.util.ArrayList;
import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

/*
 *项目名：com.example.administrator.my_first_demo.adapter
 *创建者： LJW
 *创建时间：2018/6/7 0007
 */public class WeChatAdapter extends RecyclerView.Adapter<WeChatAdapter.ViewHolder> {
    private Context mContext;

    private List<WeChatData> mList;

    //下拉刷新方法
    public void setDataSource(List<WeChatData> mdata) {
        this.mList = mdata;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_source;


        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_source = itemView.findViewById(R.id.tv_source);


        }
    }

    public WeChatAdapter(List<WeChatData> data) {
        mList = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wechat_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final WeChatData data = mList.get(position);
        holder.tv_title.setText(data.getTitle());
        holder.tv_source.setText(data.getSource());
        if (!TextUtils.isEmpty(data.getImgUrl())) {
            //加载图片
            Glide.with(getApplicationContext()).load(data.getImgUrl()).into(holder.iv_img);
        } else {
            holder.iv_img.setBackgroundResource(R.drawable.empty_data);
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
