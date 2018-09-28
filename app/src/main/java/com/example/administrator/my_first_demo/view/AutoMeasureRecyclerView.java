package com.example.administrator.my_first_demo.view;
/*
 *项目名：com.example.administrator.my_first_demo.view
 *创建者： LJW
 *创建时间：2018/8/4 0004
 *描述：
 */
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.GridView;

public class AutoMeasureRecyclerView extends GridView {

    public AutoMeasureRecyclerView(Context context) {
        super(context);
    }

    public AutoMeasureRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoMeasureRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 测量模式
     * 1.精确模式   MeasureSpec.EXACTLY
     * 2.最大模式   MeasureSpec.AT_MOST
     * 3.未指定模式 MeasureSpec.UNSPECIFIED
     * <p>
     * int 32位数值
     * 最高两位00 表示未指定数值
     * 最高两位01 表示精确模式
     * 最高两位10 表示最大模式
     *
     * @param widthSpec
     * @param heightSpec
     */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}