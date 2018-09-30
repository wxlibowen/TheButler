package com.example.administrator.my_first_demo.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 *项目名：com.example.administrator.my_first_demo.ui
 *创建者： LJW
 *创建时间：2018/6/5 0005
 */public class PhoneActivity extends BaseActivity {
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.iv_company)
    ImageView ivCompany;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_1)
    Button btn1;
    @BindView(R.id.btn_2)
    Button btn2;
    @BindView(R.id.btn_3)
    Button btn3;
    @BindView(R.id.btn_del)
    Button btnDel;
    @BindView(R.id.btn_4)
    Button btn4;
    @BindView(R.id.btn_5)
    Button btn5;
    @BindView(R.id.btn_6)
    Button btn6;
    @BindView(R.id.btn_0)
    Button btn0;
    @BindView(R.id.btn_7)
    Button btn7;
    @BindView(R.id.btn_8)
    Button btn8;
    @BindView(R.id.btn_9)
    Button btn9;
    @BindView(R.id.btn_query)
    Button btnQuery;

    //标记位
    private boolean flag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etNumber.setShowSoftInputOnFocus(false);
        }

        btnDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etNumber.setText("");
                return false;
            }
        });
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_del, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_0, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_query})
    public void onViewClicked(View view) {
        /*
        1、获取输入框值
        2、判断是否为空
        3、网络请求
        4、解析json
        5、结果显示
         */

        StringBuilder sb = new StringBuilder(etNumber.getText());
        int current = etNumber.getSelectionStart();
        switch (view.getId()) {
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_0:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if (sb.length() >= 11) return;
                if (flag) {
                    flag = false;
                    sb = null;
                    etNumber.setText("");
                }
                sb.insert(current, ((Button) view).getText());
                etNumber.setText(sb.toString());
                etNumber.setSelection(current + 1);
                //每次结尾添加1个
//                etNumber.setText(str + ((Button) view).getText());
                //移动光标
//                etNumber.setSelection(str.length() + 1);
                break;
            //获取归属地
            case R.id.btn_query:
                getPhone(sb.toString());
                break;
            //删除
            case R.id.btn_del:
                if (current == 0) return;
                if (!TextUtils.isEmpty(sb) && sb.length() > 0) {
                    sb.deleteCharAt(current - 1);
                    etNumber.setText(sb.toString());
                    etNumber.setSelection(current - 1);

                }
                break;
        }
    }

    //获取归属地
    public void getPhone(String str) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (str.matches(telRegex)) {
            String url = "http://apis.juhe.cn/mobile/get?phone=" + str + "&key=" + StaticClass.PHON_KEY;
            RxVolley.get(url, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
//                Toast.makeText(PhoneActivity.this, "结果：" + t, Toast.LENGTH_SHORT).show();
                    parsingJson(t);
                }
            });
        } else if (str == null || str.isEmpty()) {
            Toast.makeText(PhoneActivity.this, "您没有输入内容", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PhoneActivity.this, "您输入的电话号码有问题", Toast.LENGTH_SHORT).show();
        }
    }


    //解析JSON
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            String province = jsonObject1.getString("province");
            String city = jsonObject1.getString("city");
            String areacode = jsonObject1.getString("areacode");
            String zip = jsonObject1.getString("zip");
            String company = jsonObject1.getString("company");
            String card = jsonObject1.getString("card");
            tvResult.setText("归属地:" + province + city + "\n"
                    + "区号:" + areacode + "\n"
                    + "邮编:" + zip + "\n"
                    + "运营商:" + company + "\n"
                    + "类型:" + card);
            //图片显示
            switch (company) {
                case "移动":
                    ivCompany.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "联通":
                    ivCompany.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "电信":
                    ivCompany.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            flag = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
