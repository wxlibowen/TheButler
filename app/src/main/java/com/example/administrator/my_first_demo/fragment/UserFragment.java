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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_first_demo.MainActivity;
import com.example.administrator.my_first_demo.R;
import com.example.administrator.my_first_demo.application.BaseApplication;
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
import com.mob.commons.clt.FBManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.kymjs.rxvolley.toolbox.FileUtils.getExternalCacheDir;

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
    private  File outputImage;


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
                startActivity(new Intent(getActivity(), CourierActivity.class));
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
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //相机
    private void toCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //判断内存卡是否可用，可用的话就进行储存
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
//        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        outputImage = new File(getContext().getExternalCacheDir(), PHOTO_NAME);
        try {
            if (outputImage.exists()) outputImage.delete();
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT>=24){
            imageUri=FileProvider.getUriForFile(getContext(),"com.example.administrator.fileporvider",outputImage);

        }else {
            imageUri=Uri.fromFile(outputImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,CAMERA_REQUEST_CODE);







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
////                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
//                    startPhotoZoom(Uri.fromFile(outputImage));
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
                        profile_image.setImageBitmap(bitmap);
                        UtilTools.putImageToShare(getContext(),profile_image);
                        CallbackManager.doCallback();
//                        startPhotoZoom(imageUri);
//                        startPhotoZoom(Uri.fromFile(outputImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }



                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置图片，原先的应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    private Uri uritempFile;

    //裁剪图片
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.i("uri==null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片分辨率
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
//        uritempFile=uri;
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", false); // no face detection
//intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
            //没存储到share里，会出现几个模块切换的时候，再回到个人信息模块，个人头像不变。
            UtilTools.putImageToShare(getActivity(), profile_image);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存图片到ShareUtil
        UtilTools.putImageToShare(getActivity(), profile_image);
//        //保存
//        BitmapDrawable drawable = (BitmapDrawable) profile_image.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        //第一步：将bitmap压缩成字节数组输出流
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//        //第二步：利用base64将我们的字节数组输出流转换成String
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
//        //第三步：将string保存在shareUtils
//        ShareUtil.putString(getActivity(), "image_title", imgString);
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

}

