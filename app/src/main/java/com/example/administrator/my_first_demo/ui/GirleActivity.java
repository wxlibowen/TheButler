package com.example.administrator.my_first_demo.ui;
/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/8/1 0001
 *描述：
 */


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.adapter.GirlAdapter;
import com.example.administrator.my_first_demo.adapter.GirlAdapterRecycler;
import com.example.administrator.my_first_demo.adapter.WeChatAdapter;
import com.example.administrator.my_first_demo.entity.GirlData;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import uk.co.senab.photoview.PhotoViewAttacher;

public class GirleActivity extends BaseActivity {
    //列表
    private RecyclerView recyclerView;
    //数据
    private List<GirlData> list = new ArrayList<>();
    //适配器
    private GirlAdapter adapter;
    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView showimg;
    //图片地址数据
    private List<String> mlisturl = new ArrayList<>();
    //photoview
    private PhotoViewAttacher photoViewAttacher;
    //轮播图片
    private int[] imageResourceID;
    //轮播标题
    private String[] mtitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_girl);
        initView();
    }

    private void addHeader() {
        View header = getLayoutInflater().inflate(R.layout.meinv_header, null);
        Banner banner = header.findViewById(R.id.banner);
        imageResourceID = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
        mtitle = new String[]{"美女1", "美女2", "美女3", "美女4", "美女5"};
        List<Integer> imageList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < imageResourceID.length; i++) {
            imageList.add(imageResourceID[i]);
            titleList.add(mtitle[i]);
        }
        //设置加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        banner.setDelayTime(3000);//设置轮播时间
        banner.setImages(imageList);//设置图片源
        banner.setBannerTitles(titleList);//设置图片标题
        banner.start();
        adapter.addHeaderView(header);
    }


    //初始化view
    private void initView() {
        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //初始化对话框
        dialog = new CustomDialog(this, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                R.layout.dialog_girl, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //预览初始化    注意是在dialog下查找id
        showimg = dialog.findViewById(R.id.iv_image);
        //解析干货集中营美女图片接口
        String code = URLEncoder.encode("福利");
        String url = "https://gank.io/api/data/" + code + "/60/1";

        OkHttpUtils.get().url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                parSingJson(response);
            }
        });
        adapter = new GirlAdapter(this, list);
        addHeader();
        recyclerView.setAdapter(adapter);
//        监听点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Volley_Iv(mlisturl.get(position));
            }
        });
    }

    //解析JSON
    private void parSingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String url = jsonObject1.getString("url");
                mlisturl.add(url);
                GirlData data = new GirlData();
                data.setImgurl(url);
                list.add(data);
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 加载图片
    protected void Volley_Iv(final String imgUrl) {
        Picasso.with(GirleActivity.this).load(imgUrl).into(showimg);
        photoViewAttacher = new PhotoViewAttacher(showimg);
        photoViewAttacher.update();
        dialog.show();
    }
}
