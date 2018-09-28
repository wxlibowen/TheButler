package com.example.administrator.my_first_demo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;

import com.example.administrator.my_first_demo.adapter.CourierAdapterRecycler;
import com.example.administrator.my_first_demo.entity.CourierData;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.google.gson.JsonObject;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/5 0005
 * 描述：快递
 */public class CourierActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_number;
    private Button btn_get_courier;
    //    private ListView mListView;
    public List<CourierData> mlist = new ArrayList<>();
    public RecyclerView recyclerView;
   //清空输入框
    private ImageView deletCompany,deletNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();

    }

    //初始化View
    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        recyclerView = findViewById(R.id.mRecyclerView);
        btn_get_courier = findViewById(R.id.btn_get_courier);
        btn_get_courier.setOnClickListener(this);
        deletCompany =findViewById(R.id.delete_company);
        deletCompany.setOnClickListener(this);
        deletNumber =findViewById(R.id.delete_number);
        deletNumber.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //清空输入框
            case R.id.delete_company:
                et_name.setText("");
                break;
            case R.id.delete_number:
                et_number.setText("");
                break;
            case R.id.btn_get_courier:
                /**
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.拿到数据去请求数据（Json）
                 * 4.解析Json
                 * 5.listview适配器
                 * 6.实体类（item）
                 * 7.设置数据/显示效果
                 */

                //1.获取输入框的内容
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();
                //拼接我们的url
                String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.COURIER_KEY
                        + "&com=" + name + "&no=" + number;

                //2.判断是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                    //3.拿到数据去请求数据（Json）
                    RxVolley.get(url, new HttpCallback() {
                                @Override
                                public void onSuccess(String t) {
                                    //Toast.makeText(CourierActivity.this, t, Toast.LENGTH_SHORT).show();
                                    L.i("Courier:" + t);
                                    //4.解析Json
                                    parsingJson(t);
                                }
                                //查无弹出对话框
                            }
                    );

                } else {
                    Toast.makeText(this, "你输入为空，请重新输入", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                CourierData data = new CourierData();
                data.setRemark(object.getString("remark"));
                data.setZone(object.getString("zone"));
                data.setDatetime(object.getString("datetime"));
                mlist.add(data);
            }
            //倒序
            Collections.reverse(mlist);
//            CourierAdapter adapter =new CourierAdapter(this,mlist);
//            mListView.setAdapter(adapter);
            CourierAdapterRecycler adapter = new CourierAdapterRecycler(mlist);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
