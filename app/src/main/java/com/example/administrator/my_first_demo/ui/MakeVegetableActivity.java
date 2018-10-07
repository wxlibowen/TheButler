package com.example.administrator.my_first_demo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.adapter.MakeVegetableAdapter;
import com.example.administrator.my_first_demo.base.BaseActivity;
import com.example.administrator.my_first_demo.entity.MakeVegetableData;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/6 0006
 * 描述：做菜大全
 */public class MakeVegetableActivity extends BaseActivity implements View.OnClickListener {
    private EditText make_text, make_show_number;
    private Button make_search;
    private List<MakeVegetableData> mlist = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makevegetable);
        initView();
    }

    private void initView() {
        make_text = findViewById(R.id.et_make_text);
        make_show_number = findViewById(R.id.make_show_number);
        make_search = findViewById(R.id.btn_start);
        make_search.setOnClickListener(this);
        listView = findViewById(R.id.lv_makevegetable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                //获取输入框内容
                String maketext = make_text.getText().toString().trim();
                String makeshownumber = make_show_number.getText().toString().trim();
                //拼接url
                String url = "http://apis.juhe.cn/cook/query?key=" + StaticClass.MAKE_VEGETABLE + "&menu=" + maketext+"&rn="+makeshownumber;
                //判断是否为空
                if (!TextUtils.isEmpty(maketext)) {
                    //拿到数据请求JSOn
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            L.i("Courier:" + t);
                            //4.解析Json
                            parsingJson(t);
                        }

                    });

                } else {
                    Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析JSOn
    private void parsingJson(String t) {
        try {
            JSONObject object = new JSONObject(t);
            JSONObject object1 = object.getJSONObject("result");
            JSONArray jsonArray = object1.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MakeVegetableData data = new MakeVegetableData();
                data.setTitle(jsonObject.getString("title"));
                data.setBurden(jsonObject.getString("burden"));
                data.setImtro(jsonObject.getString("imtro"));
                data.setIngredients(jsonObject.getString("ingredients"));
                  JSONArray jsonArray1 =jsonObject.getJSONArray("steps");
                  for (int j=0;j<jsonArray1.length();j++){
                      JSONObject jsonObject1 =jsonArray1.getJSONObject(j);
                      data.setStepdetail(jsonObject1.getString("step"));
                  }
                mlist.add(data);
            }
            MakeVegetableAdapter adapter = new MakeVegetableAdapter(this, mlist);
            listView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
