package com.example.administrator.my_first_demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.CourierData;

import java.util.List;

/*
 *项目名：com.example.administrator.my_first_demo.adapter
 *创建者： LJW
 *创建时间：2018/6/5 0005
 * 描述：快递适配器
 */
public class CourierAdapterRecycler extends RecyclerView.Adapter<CourierAdapterRecycler.ViewHoler> {
    private List<CourierData> courierDatalist;


    static class ViewHoler extends RecyclerView.ViewHolder {
        TextView tv_remark;
        TextView tv_zone;
        TextView tv_datetime;

        public ViewHoler(View view) {
            super(view);
            //获取布局实例
            tv_datetime = view.findViewById(R.id.tv_datetime);
            tv_remark = view.findViewById(R.id.tv_remark);
            tv_zone = view.findViewById(R.id.tv_zone);
        }
    }

    //把要展示的数据源加载进来
    public CourierAdapterRecycler(List<CourierData> courierDatalist) {
        this.courierDatalist = courierDatalist;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_courier_item, null);
        ViewHoler holder = new ViewHoler(view);//将加载的布局传入构造函数内
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        CourierData courierData = courierDatalist.get(position);
        holder.tv_datetime.setText(courierData.getDatetime());
        holder.tv_zone.setText(courierData.getZone());
        holder.tv_remark.setText(courierData.getRemark());
    }

    @Override
    public int getItemCount() {
        return courierDatalist.size();
    }


}
