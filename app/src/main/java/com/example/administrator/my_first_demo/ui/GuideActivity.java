package com.example.administrator.my_first_demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.my_first_demo.MainActivity;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.utils.L;

import java.util.ArrayList;
import java.util.List;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/3 0003
 * 描述：引导页
 */public class GuideActivity extends Activity implements View.OnClickListener {
    private ViewPager mViewPager;
    //容器
    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;
    //小圆点
    private ImageView point1, point2, point3;
    //跳过
    private ImageView iv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    public void initView() {
        mViewPager = findViewById(R.id.mViewPager);
        view1 = View.inflate(this, R.layout.pager_item_one, null);
        view2 = View.inflate(this, R.layout.pager_item_two, null);
        view3 = View.inflate(this, R.layout.pager_item_three, null);
        view3.findViewById(R.id.skip_to_main).setOnClickListener(this);

        point1 = findViewById(R.id.point_one);
        point2 = findViewById(R.id.point_two);
        point3 = findViewById(R.id.point_three);
        iv_skip = findViewById(R.id.iv_back);
        iv_skip.setOnClickListener(this);
        //设置默认图片
        setPointImg(true, false, false);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        //设置适配器
        mViewPager.setAdapter(new GuideAdapter());
        //监听Viewpager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                L.i("position" + position);
                switch (position) {
                    case 0:
                        iv_skip.setVisibility(View.VISIBLE);
                        setPointImg(true, false, false);
                        break;
                    case 1:
                        iv_skip.setVisibility(View.VISIBLE);
                        setPointImg(false, true, false);
                        break;
                    case 2:
                        iv_skip.setVisibility(View.GONE);
                        setPointImg(false, false, true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_to_main:
            case R.id.iv_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        //添加原点
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewGroup) container).addView(mList.get(position));
            return mList.get(position);
        }

        //移除原点
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewGroup) container).removeView(mList.get(position));
            //  super.destroyItem(container, position, object);
        }
    }

    //设置小圆点的选中效果
    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3) {
        //可修改为switch来写
        if (isCheck1) {
            point1.setBackgroundResource(R.drawable.point_on);
        } else {
            point1.setBackgroundResource(R.drawable.point_off);
        }
        if (isCheck2) {
            point2.setBackgroundResource(R.drawable.point_on);
        } else {
            point2.setBackgroundResource(R.drawable.point_off);
        }
        if (isCheck3) {
            point3.setBackgroundResource(R.drawable.point_on);
        } else {
            point3.setBackgroundResource(R.drawable.point_off);
        }
    }
}
