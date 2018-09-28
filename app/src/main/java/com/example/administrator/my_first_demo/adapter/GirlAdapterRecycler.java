package com.example.administrator.my_first_demo.adapter;
/*
 *项目名: My_First_Demo
 *文件名: GirlAdapterRecycler
 *创建者: LJW
 *创建时间:2018/9/13 0013 9:49
 *描述:
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.GirlData;
import com.example.administrator.my_first_demo.utils.PicassoUtils;

import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class GirlAdapterRecycler extends RecyclerView.Adapter<GirlAdapterRecycler.ViewHolder> {
    private Context context;
    private List<GirlData> mList;
    private LayoutInflater inflater;
//    private WindowManager wm;
//    //屏幕宽
//    private int width;

    public GirlAdapterRecycler(Context context, List<GirlData> mList) {
        this.context = context;
        this.mList = mList;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        width = wm.getDefaultDisplay().getWidth();
    }

    //找到控件
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageview);
        }
    }

    //加载布局
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.girl_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //赋值
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GirlData data = mList.get(position);
        if (!TextUtils.isEmpty(data.getImgurl())){
            Glide.with(getApplicationContext()).load(data.getImgurl()).into(holder.img);
        }else {
            holder.img.setBackgroundResource(R.drawable.empty_data);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
