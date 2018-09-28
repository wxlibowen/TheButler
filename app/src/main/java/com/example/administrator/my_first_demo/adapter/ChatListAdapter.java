package com.example.administrator.my_first_demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.ChatListDate;
import com.example.administrator.my_first_demo.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

import rx.internal.schedulers.EventLoopsScheduler;

/*
 *项目名：com.example.administrator.my_first_demo.adapter
 *创建者： LJW
 *创建时间：2018/6/7 0007
 */
public class ChatListAdapter extends BaseAdapter {
    //左右两边type
    public static final int VALUE_LEFT_TEXT = 1;
    public static final int VALUE_RIGHT_TEXT = 2;
    private Context context;
    private ArrayList<ChatListDate> list;
    private Bitmap userImage;

    public ChatListAdapter(Context context, ArrayList<ChatListDate> list,Bitmap userImage) {
        this.context = context;
        this.list = list;
        this.userImage=userImage;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeftText viewHolderLeftText = null;
        ViewHolderRightText viewHolderRightText = null;

        //获取当前要显示的type，根据type来区分数据的加载
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    convertView = LayoutInflater.from(context).inflate(R.layout.left_item, null);
                    viewHolderLeftText.tv_left_text = convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeftText);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = new ViewHolderRightText();
                    convertView = LayoutInflater.from(context).inflate(R.layout.right_item, null);
                    viewHolderRightText.tv_right_text = convertView.findViewById(R.id.tv_right_text);
                    viewHolderRightText.iv_butler = convertView.findViewById(R.id.iv_butler);
                    if (userImage!=null){
                        viewHolderRightText.iv_butler.setImageBitmap(userImage);
                    }

                    convertView.setTag(viewHolderRightText);
                    break;
            }
            }else{
            switch (type){
                case VALUE_LEFT_TEXT:
              viewHolderLeftText  = (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText  = (ViewHolderRightText) convertView.getTag();

            }
        }


        //赋值
        ChatListDate date =list.get(position);
        switch (type){
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(date.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRightText.tv_right_text.setText(date.getText());



        }

        return convertView;
    }

    //根据数据源的position来返回要显示的item
    @Override
    public int getItemViewType(int position) {
        ChatListDate chatListDate = list.get(position);
        int type = chatListDate.getType();
        return type;
    }

    //返回所以得layout数量
    @Override
    public int getViewTypeCount() {
        return 3;//相当于list.size+1
    }

    //左边的文本
    class ViewHolderLeftText {
        private TextView tv_left_text;
    }

    //右边的文本
    class ViewHolderRightText {
        private TextView tv_right_text;
        private  ImageView iv_butler;


    }

}
