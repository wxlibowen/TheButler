package com.example.administrator.my_first_demo.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.adapter.ChatListAdapter;
import com.example.administrator.my_first_demo.entity.ChatListDate;
import com.example.administrator.my_first_demo.utils.CallbackManager;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.utils.MyCallback;
import com.example.administrator.my_first_demo.utils.ShareUtil;
import com.example.administrator.my_first_demo.utils.StaticClass;
import com.example.administrator.my_first_demo.utils.UtilTools;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

/*
 *项目名：com.example.administrator.my_first_demo.fragment
 *创建者： LJW
 *创建时间：2018/6/3 0003
 * 描述：自动聊天
 */public class  ButlerFragment extends Fragment implements View.OnClickListener ,MyCallback {

    //上下文
//    private Context context;
    //适配器
    private ChatListAdapter chatListAdapter;
    //数据源
    private ArrayList<ChatListDate> list = new ArrayList<>();
    //listview列表
    private ListView mChatListView;
    private Button btn_send;
    private EditText input_text;
    //第二次进来不会再显示小李
    private boolean forwu = true;
    //TTS
    private SpeechSynthesizer mTts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_butler, container, false);
        }
        // 缓存的viewiew需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        forceOpenSoftKeyboard(getActivity());
        initView(view);
        CallbackManager.setCallback(this);
        return view;
    }

    private void initView(View view) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        mChatListView = view.findViewById(R.id.mChatListView);
        btn_send = view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        input_text = view.findViewById(R.id.et_text);
        //设置适配器
        Bitmap userImage=UtilTools.getImageBitmapToShare(getContext());
        chatListAdapter = new ChatListAdapter(getContext(), list,userImage);

        mChatListView.setAdapter(chatListAdapter);

        if (forwu) {
            addLeftItem("您好，我是智能机器人");
            forwu = false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                //获取输入框内容，判空，清空输入框，发送机器人请求返回内容，添加输入内容到right item,拿到机器人返回值放到letf item
                String text = input_text.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    //清空输入框
                    input_text.setText("");
                    addRightItem(text);
//                    接口1
                    String url = "http://www.tuling123.com/openapi/api";
                    HttpParams params = new HttpParams();
                    params.put("key", StaticClass.CHAT_LIST_KEY);
                    params.put("info", text);

//                    String url = "http://openapi.tuling123.com/openapi/api/v2";
//                    HttpParams params = new HttpParams();
//                    params.put("userInfo/apiKey=", StaticClass.CHAT_LIST_KEY);
//                    params.put("perception/inputText/text", text);
//                    params.put("userInfo/userId", "161666");
                    RxVolley.post(url, params, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            L.i(t);
                            parsingJson(t);
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //关闭软键盘在Fragment的强制关法
    public void forceOpenSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    //解析JSOn
    private void parsingJson(String t) {
        try {
            //接口1 解析
            JSONObject jsonObject = new JSONObject(t);
            String text = jsonObject.getString("text");
            addLeftItem(text);

//            JSONObject jsonObject = new JSONObject(t);
//            JSONArray array = jsonObject.getJSONArray("results");
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject jsonObject1 = (JSONObject) array.get(i);
//                jsonObject1.getString("resultType");
//                jsonObject1.getString("groupType");
//                JSONObject object = jsonObject1.getJSONObject("values");
//                String text = object.getString("text");
//                addLeftItem(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text) {
        boolean isSpeak = ShareUtil.getBoolean(getActivity(), "isSpeak", false);
        if (isSpeak) {
            startSpeak(text);
        }
        ChatListDate date = new ChatListDate();
        date.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        date.setText(text);
        list.add(date);
        //刷新adapter
        chatListAdapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }





    //添加右边文本
    private void addRightItem(String text) {
        ChatListDate date = new ChatListDate();
        date.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        date.setText(text);
        list.add(date);
        //刷新adapter
        chatListAdapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //开始说话
    private void startSpeak(String text) {
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };


    @Override
    public void changeAdapter() {
        L.i("lbw调用了接口回调");
        Bitmap userImage=UtilTools.getImageBitmapToShare(getContext());
        chatListAdapter = new ChatListAdapter(getContext(), list,userImage);
        mChatListView.setAdapter(chatListAdapter);

    }
}
