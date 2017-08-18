package com.example.administrator.retrofit_rxjava.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.retrofit_rxjava.ImageUtil;
import com.example.administrator.retrofit_rxjava.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 选择图片的方式（拍照或者从图库选择）
     */
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
            Glide.with(this).load(filePath).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivHead);
        }
        if (file_list.size() != 0) {
            progressDialog.show();
            // upload();
            //upload_avatar();
            upload_2();
        }
    }


}
