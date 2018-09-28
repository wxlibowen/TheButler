package com.example.administrator.my_first_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.CourierData;
import com.example.administrator.my_first_demo.entity.MakeVegetableData;

import java.util.List;

/*
 *项目名：com.example.administrator.my_first_demo.adapter
 *创建者： LJW
 *创建时间：2018/6/6 0006
 */public class MakeVegetableAdapter extends BaseAdapter {
    private Context mContext;
    private List<MakeVegetableData> mList;
    //布局加载器
    private LayoutInflater inflater;
    private MakeVegetableData data;

    public MakeVegetableAdapter(Context mContext, List<MakeVegetableData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //第一次加载
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_makevegetable_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_mv_title);
            viewHolder.burden = convertView.findViewById(R.id.tv_mv_tiaoliao);
            viewHolder.imtro = convertView.findViewById(R.id.tv_mv_desc);
            viewHolder.ingredients = convertView.findViewById(R.id.tv_mv_shicai);
            viewHolder.step_detail = convertView.findViewById(R.id.tv_step_detail);
            //设置缓存
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        data = mList.get(position);

        viewHolder.title.setText(data.getTitle());
        viewHolder.imtro.setText(data.getImtro());
        viewHolder.ingredients.setText(data.getIngredients());
        viewHolder.burden.setText(data.getBurden());
        viewHolder.step_detail.setText(data.getStepdetail());
        return convertView;
    }

    static class ViewHolder {
        private TextView title;
        private TextView imtro;//简介
        private TextView ingredients;//食材
        private TextView burden;//调料
        private TextView step_detail;//步骤详情
    }
}
