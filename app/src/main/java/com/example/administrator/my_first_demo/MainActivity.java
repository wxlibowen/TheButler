package com.example.administrator.my_first_demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.my_first_demo.fragment.ButlerFragment;
import com.example.administrator.my_first_demo.fragment.JoyNewsFragment;
import com.example.administrator.my_first_demo.fragment.StarnewsFragment;
import com.example.administrator.my_first_demo.fragment.UserFragment;
import com.example.administrator.my_first_demo.fragment.WeChatFragment;
import com.example.administrator.my_first_demo.ui.AboutActivity;
import com.example.administrator.my_first_demo.base.BaseActivity;
import com.example.administrator.my_first_demo.ui.GirleActivity;
import com.example.administrator.my_first_demo.ui.LocationActivity;
import com.example.administrator.my_first_demo.ui.QrCodeActivity;
import com.example.administrator.my_first_demo.ui.UpdateActivity;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页Activity
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViesPager;
    //Title
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragment;
    //版本名，版本号
    private String versionName;
    private int versioncode;
    private String url;
    //退出app
    private boolean canExit = false;
    private Handler handler = new Handler();

    //    //悬浮窗
//    private FloatingActionButton floatingActionButton;
//drawerLayout
    private DrawerLayout drawerLayout;
    //侧滑菜单栏
    private NavigationView navigationView;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        //toolbar初始化
        initToolbar();
        //初始化
        initView();

        //内存泄漏测试方法
//        LeakThread leakThread = new LeakThread();
//        leakThread.start();

        //Bugly测试方法
        //CrashReport.testJavaCrash();

    }

    //内存泄漏测试方法
//    class LeakThread extends Thread {
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(6 * 60 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        RefWatcher refWatcher = BaseApplication.getRefWatcher(this);//1
//        refWatcher.watch(this);
//    }
//toolbar初始化
    private void initToolbar() {

    }

    //初始化View
    private void initView() {
        mtoolbar = findViewById(R.id.mToolbar);

        setSupportActionBar(mtoolbar);
        //侧滑相关
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);

//        UtilTools.getImageToShare(this,iv_avatar);

        //切换菜单（左上角）注意第三个和第四个参数,需要在string里面声明，注意把toolbar参数放进去不然就没有切换的那个按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, mtoolbar,
                R.string.openDrawerContentDescRes,
                R.string.closeDrawerContentDescRes);
        //切换菜单的动画
        drawerLayout.addDrawerListener(toggle);
        //同步状态
        toggle.syncState();
        //开启手势滑动打开侧滑菜单栏，如果要关闭将后面的UNLOCKED替换成LOCKED_CLOSED就可以了
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //悬浮按钮
//        floatingActionButton = findViewById(R.id.fab_setting);
//        floatingActionButton.setOnClickListener(this);
        //浮动按钮默认隐藏，让第一页（语音助手没有悬浮窗）
//        floatingActionButton.setVisibility(View.GONE);
        mTabLayout = findViewById(R.id.mTabLayout);
        mViesPager = findViewById(R.id.mViewPager);
        //预加载  绑定让他们可以对应滑动
        mViesPager.setOffscreenPageLimit(mFragment.size());
        //mViewPager滑动监听,如果要显示浮动按钮才写监听事件
//        mViesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                L.i("position" + position);
//                if (position == 0) {
//                    //因为有四个页面，0代表的是第一个页面让其悬浮窗没掉
//                    floatingActionButton.setVisibility(View.GONE);
//                } else {
//                    floatingActionButton.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        //设置适配器
        mViesPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            // 选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViesPager);
        //为tablayout设置图标
//        mTabLayout.getTabAt(0).setIcon(R.drawable.item1_before);
//        mTabLayout.getTabAt(1).setIcon(R.drawable.item5_before);
//        mTabLayout.getTabAt(2).setIcon(R.drawable.item3_before);
//        mTabLayout.getTabAt(3).setIcon(R.drawable.item4_before);
    }

    //初始化数据
    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add(getString(R.string.guanjia));
        mTitle.add(getString(R.string.weixin));
        mTitle.add("电影新闻");
        mTitle.add("星座精选");
//        mTitle.add(getString(R.string.meinv));
        mTitle.add(getString(R.string.geren));
        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WeChatFragment());
        mFragment.add(new JoyNewsFragment());
        mFragment.add(new StarnewsFragment());
        mFragment.add(new UserFragment());

    }


    //侧滑界面点击
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //美女
            case R.id.voice_switch:
                startToActivity(GirleActivity.class);
                break;
            case R.id.version:
                /**
                 * 1、请求服务器的配置文件，拿到code
                 * 2、比较
                 * 3、dialog提示
                 * 4、跳转到更新界面，并且把url传递过去
                 */
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        L.i(t);
                        parsingJson(t);
                    }
                });
                break;
            case R.id.location:
                startToActivity(LocationActivity.class);
                break;
            case R.id.about:
                startToActivity(AboutActivity.class);
                break;
            case R.id.scan:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.code:
                startToActivity(QrCodeActivity.class);
                break;
        }
        return true;
    }

    //解析json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if (code > versioncode) {
                showUpdateDialog(jsonObject.getString("content"));
            } else {
                Toast.makeText(this, "当前为最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //获取版本号
    private void getVersionNameCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versioncode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this).setTitle("有新版本").setMessage("修复多项bug").
                setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(MainActivity.this, UpdateActivity.class);
                        it.putExtra("url", url);
                        startActivity(it);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        if (!canExit) {
            canExit = true;
            Snackbar.make(drawerLayout, " 再次点击退出", 2000).show();
            handler.postDelayed(runnable, 2000);//两秒后不允许退出
        } else {
            super.onBackPressed();
        }


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            canExit = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
