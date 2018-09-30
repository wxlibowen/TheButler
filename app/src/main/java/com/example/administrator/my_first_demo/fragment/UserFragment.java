package com.example.administrator.my_first_demo.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.entity.MyUser;
import com.example.administrator.my_first_demo.ui.CourierActivity;
import com.example.administrator.my_first_demo.ui.EditUserInformation;
import com.example.administrator.my_first_demo.ui.LoginActivity;
import com.example.administrator.my_first_demo.ui.MakeVegetableActivity;
import com.example.administrator.my_first_demo.ui.PhoneActivity;
import com.example.administrator.my_first_demo.utils.CallbackManager;
import com.example.administrator.my_first_demo.utils.L;
import com.example.administrator.my_first_demo.utils.ShareUtil;
import com.example.administrator.my_first_demo.utils.UtilTools;
import com.example.administrator.my_first_demo.view.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 *项目名：com.example.administrator.my_first_demo.fragment
 *创建者： LJW
 *创建时间：2018/6/3 0003
 * 描述：个人中心
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    //退出登录
    private Button btn_exit_user;
    //编辑资料
    private TextView edit_user;

    //圆形头像
    private CircleImageView profile_image;
    private CustomDialog dialog;
    //对话框中的三个按钮
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    //快递查询
    private RelativeLayout tv_courier;
    //归属地查询
    private RelativeLayout tv_phone;
    //菜谱查询
    private RelativeLayout tv_Menu;
    //管家语音开关
    private Switch voice_turn;


    //相机拍摄取图相关
    public static final int TAKE_PHOTO = 1;
    public final String PHOTO_NAME = "output_image.jpg";
    private Uri imageUri;
    private File outputImage;
    private Bitmap bitmap;//用户头像
    private MyHandler handler = new MyHandler();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //退出
        btn_exit_user = view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        //编辑
        edit_user = view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        //快递
        tv_courier = view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        //号码归属地
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);
        //菜谱
        tv_Menu = view.findViewById(R.id.tv_menu);
        tv_Menu.setOnClickListener(this);
        //管家语音开关
        voice_turn = view.findViewById(R.id.sw_speak);
        voice_turn.setOnClickListener(this);
        profile_image = view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        //从ShareUtil取出头像
        UtilTools.getImageToShare(getActivity(), profile_image);

        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

//        //默认是不可点击的/不可输入
//        setEnabled(false);

//        //设置具体的值
//        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
//        et_username.setText(userInfo.getUsername());
//        et_age.setText(userInfo.getAge() + "");
//        et_sex.setText(userInfo.isSex() ? getString(R.string.text_boy) : getString(R.string.text_girl));
//        et_desc.setText(userInfo.getDesc());
        //V3.4.5版本开始设置具体值得另一种方法
//        //BmobUser中的特定属性
//        etUsername.setText((String) BmobUser.getObjectByKey("username"));
//        //MyUser中的扩展属性
//        etAge.setText((Integer) BmobUser.getObjectByKey("age")+"");
//        etDesc.setText((String) BmobUser.getObjectByKey("desc"));
//        etSex.setText((Boolean) BmobUser.getObjectByKey("sex")?"男":"女");
    }


    private boolean isCamare = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退出登录
            case R.id.btn_exit_user:
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            //编辑资料
            case R.id.edit_user:
                startActivity(new Intent(getActivity(), EditUserInformation.class));
                break;
            //显示头像
            case R.id.profile_image:
                dialog.show();
                break;
            //取消
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            //相机
            case R.id.btn_camera:


                isCamare = true;
                //6.0以上系统需要 运行时权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    check();
                } else {
                    toCamera();
                }
                break;
            //相册
            case R.id.btn_picture:
                toPicture();
                break;
            //快递查询
            case R.id.tv_courier:
                startActivity(new Intent(getContext(), CourierActivity.class));
                break;
            //归属地查询
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
            //做菜查询
            case R.id.tv_menu:
                startActivity(new Intent(getActivity(), MakeVegetableActivity.class));
                break;
            //管家语音开关
            case R.id.sw_speak:
                //切换相反
                voice_turn.setSelected(!voice_turn.isSelected());
                //保存状态
                ShareUtil.putBoolean(getActivity(), "isSpeak", voice_turn.isChecked());
                break;

        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private static final int PEMISSIN_CAMARA = 103;
    private File tempFile = null;

    //相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //第一行代码
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //相机
    private void toCamera() {
        outputImage = new File(getContext().getExternalCacheDir(), PHOTO_NAME);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(getContext(), "com.example.administrator.provider", outputImage);

        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            switch (requestCode) {
                //相册返回数据
                case IMAGE_REQUEST_CODE:
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                    break;
                //相机返回数据
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(imageUri);
                    break;
                //裁剪后的返回值
                case RESULT_REQUEST_CODE:
                    setImageToView();
                    break;
            }
        }
    }


    //裁剪
    private void startPhotoZoom(Uri uri) {
        File CropPhoto = new File(getContext().getExternalCacheDir(), "Crop.jpg");//这个是创建一个截取后的图片路径和名称。
        try {
            if (CropPhoto.exists()) {
                CropPhoto.delete();
            }
            CropPhoto.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(CropPhoto);
        L.i("LBW uri:" + uri);
        L.i("LBW imageUri:" + imageUri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //输出的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


    private void setImageToView() {
        try {
            bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        profile_image.setImageBitmap(bitmap);
        new Thread(new Runnable() {
            @Override
            public void run() {
                UtilTools.putImageToShare(getContext(), bitmap);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }


    //Android6.0动态权限添加
    private List<String> permissionList;

    private void check() {
        //第一步检查是否有权限
        permissionList = new ArrayList<>();
        if (isCamare && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        //有权限未申请
        if (!permissionList.isEmpty()) {
            //第一次申请权限，如果用户拒绝，下次申请需向用户解释为什么要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    permissionList.get(0))) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("设置头像需要权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请权限
                                ActivityCompat.requestPermissions(getActivity(),
                                        permissionList.toArray(new String[permissionList.size()]), PEMISSIN_CAMARA);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissionList.toArray(new String[permissionList.size()]), PEMISSIN_CAMARA);
            }
        } else {
            //权限都已经申请
            if (isCamare) {
                toCamera();
            } else {
                toPicture();
            }
        }
    }

    //权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PEMISSIN_CAMARA) {
            if (grantResults.length > 0) {
                for (int grantReuslt : grantResults) {
                    if (grantReuslt != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "权限已被禁止,请到设置处打开权限", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (isCamare) {
                    toCamera();
                } else {
                    toPicture();
                }
            }
        }

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        CallbackManager.doCallback();
                        break;
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

    }
}

