package com.example.administrator.retrofit_rxjava.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.retrofit_rxjava.R;
import com.example.administrator.retrofit_rxjava.network.RetrofitService;
import com.example.administrator.retrofit_rxjava.network.RetrofitServiceUtil;
import com.example.administrator.retrofit_rxjava.util.FileUtil;
import com.example.administrator.retrofit_rxjava.util.ImageUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SingleUploadActivity extends AppCompatActivity {

    private ImageView iv;
    private String filePath;
    private File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_upload);
        iv = (ImageView) findViewById(R.id.iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dir = new File(FileUtil.getDiskCacheDir(SingleUploadActivity.this) + "/" + "avatar");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, "avatar.jpg");
                filePath = file.getAbsolutePath();
                intentChooser(filePath);
            }
        });
    }

    /**
     * 照片选择器
     *
     * @param path 保存图片的路径
     */
    private void intentChooser(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        //图库选择的Intent
        //图库选择的Intent
        Intent storeIntent = new Intent();
        storeIntent.setType("image/*");//可选择图片视频
        //修改为以下两句代码
        storeIntent.setAction(Intent.ACTION_PICK);
        storeIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//使用以上这种模式，并添加以上两句

        //拍照的intent
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        //创建IntentChoose
        Intent choose = Intent.createChooser(storeIntent, "请选择...");
        choose.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{photoIntent});

        // 发送chooser
        startActivityForResult(choose, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //data等于空意味着指定了具体的路径，所以data=null
            if (requestCode == 101 && data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                Cursor c = cr.query(uri, null, null, null, null);
                assert c != null;
                c.moveToNext();

                // 该图像在SD卡上地址
                String path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                ImageUtil.compressPicture(path, filePath, 300, 300, 30);
                c.close();
            } else {
                ImageUtil.compressPicture(filePath, filePath, 300, 300, 30);
            }
            Glide.with(SingleUploadActivity.this).load(filePath).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
        }
        if (new File(filePath).exists()) {
            upload_avatar();
        }
    }

    //上传图片(方法一)
    private void upload_avatar() {
        RetrofitService retrofitService = RetrofitServiceUtil.getRetrofitService();
        RequestBody params = RequestBody.create(MediaType.parse("text/plain"), "MdT4A1xdN8zcMd29N83fw0x0ObD2M0z4NeT9Q800N0z9k928NeQcO0O0OfO0O0O8");
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/png/jpg; charset=utf-8"), new File(filePath));
        MultipartBody.Part part = MultipartBody.Part.createFormData("headimgurl", "avatar", requestBody);
        retrofitService.upload_avatar(part, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(SingleUploadActivity.this, "上传完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("111", "error =" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Log.e("111", "result =" + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //上传图片（方法二）
    private void upload_1() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("auth", "MdT4A1xdN8zcMd29N83fw0x0ObD2M0z4NeT9Q800N0z9k928NeQcO0O0OfO0O0O8");
        builder.addFormDataPart("headimgurl", "avatar", RequestBody.create(MediaType.parse("image/png/jpg; charset=utf-8"), new File(filePath)));
        RetrofitService retrofitService = RetrofitServiceUtil.getRetrofitService();
        retrofitService.upload_avatar(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(SingleUploadActivity.this, "上传完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("111", "error =" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Log.e("111", "result =" + responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
