package com.example.administrator.my_first_demo.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.MainActivity;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.adapter.WeChatAdapter;
import com.example.administrator.my_first_demo.entity.RecyclerItemClickListener;
import com.example.administrator.my_first_demo.entity.WeChatData;
import com.example.administrator.my_first_demo.ui.WebViewActivity;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/*
 *项目名：com.example.administrator.my_first_demo.fragment
 *创建者： LJW
 *创建时间：2018/6/3 0003
 * 描述：微信新闻
 */
public class WeChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<WeChatData> mList = new ArrayList<>();
    //标题
    private List<String> mListTitle = new ArrayList<>();
    //地址
    private List<String> mListUrl = new ArrayList<>();

    //上拉刷新框架
    Handler mhandler;
    private RefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);
        findView(view);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        return view;
    }

    //初始化View
    private void findView(View view) {
        //recyclerview初始化
        recyclerView = view.findViewById(R.id.wechat_mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //handler初始化
        mhandler = new Handler();
        //下拉刷新控件
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDataSource();
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDataSource();
                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.autoRefresh();
        //解析接口
        //String url = "http://v.juhe.cn/toutiao/index?type=" + "shehui" + "&key=" + StaticClass.WECHAT_KEY;
        //http://apicloud.mob.com/wx/article/search?page=1&cid=5&key=520520test&size=20
        String url = "http://apicloud.mob.com/wx/article/search?page=1&cid=2&" + "&key=" + StaticClass.NEWS_KEY + "&size=60";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
//                Toast.makeText(getActivity(),t,Toast.LENGTH_SHORT).show();
                parsingJson(t);

            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("title", mListTitle.get(position));
                        intent.putExtra("url", mListUrl.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int posotion) {

                    }
                }));
    }

    private void initDataSource() {
        WeChatAdapter adapter = new WeChatAdapter(mList);
        adapter.setDataSource(mList);
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonresult = jsonObject.getJSONObject("result");
            JSONArray jsonList = jsonresult.getJSONArray("list");
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject json = (JSONObject) jsonList.get(i);
                WeChatData data = new WeChatData();
                String titlr = json.getString("title");
                String url = json.getString("sourceUrl");
                data.setTitle(titlr);
                data.setSource(json.getString("subTitle"));
                data.setImgUrl(json.getString("thumbnails"));

                mList.add(data);

                mListTitle.add(titlr);
                mListUrl.add(url);
            }

            WeChatAdapter adapter = new WeChatAdapter(mList);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

