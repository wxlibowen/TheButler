package com.example.administrator.my_first_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.GirlData;
import com.example.administrator.my_first_demo.entity.WeChatData;
import com.example.administrator.my_first_demo.utils.PicassoUtils;

import java.util.List;

/*
 *项目名：com.example.administrator.my_first_demo.adapter
 *创建者： LJW
 *创建时间：2018/6/9 0009
 * 描述：美女适配器
 */public class GirlAdapter extends BaseQuickAdapter<GirlData, BaseViewHolder> {
    private Context mContext;
    public GirlAdapter(Context context, List<GirlData> mList) {
        super(R.layout.girl_item, mList);
        this.mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, GirlData item) {
        ImageView imageView = helper.getView(R.id.imageview);
        Glide.with(mContext).load(item.getImgurl()).into(imageView);
    }
}
