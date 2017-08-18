package com.example.administrator.retrofit_rxjava.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.retrofit_rxjava.util.FileUtil;
import com.example.administrator.retrofit_rxjava.R;
import com.example.administrator.retrofit_rxjava.network.RetrofitServiceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DownLoadActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        btn = (Button) findViewById(R.id.btn_download);

        initProgressDialog();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                File dir = new File(FileUtil.getDiskCacheDir(DownLoadActivity.this) + "/apk");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //命名下载后的文件，可以自己更改。(文件的路径全部可以自己更改)
                File file = new File(dir, "捎点宝.apk");  //如 File file = new File(dir,"***.apk");
                String path = file.getAbsolutePath();
                String url = "http://shaodianbao.com/download/shaoke_v1.2.apk";
                apkDownLoad(url, path);
            }
        });

    }


    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("版本更新");
        dialog.setProgressStyle(dialog.STYLE_HORIZONTAL);    //设置进度条未圆形转动的进度条
        dialog.setCancelable(false);          //设置是否可以通过back键取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMax(100);
        dialog.setProgress(0);

    }

    /**
     * 新版本的下载
     *
     * @param url      下载路径
     * @param savePath 保存路径
     */
    private void apkDownLoad(String url, final String savePath) {
        RetrofitServiceUtil.getRetrofitService().apk_download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        //切换回了主线程
                        float hasRead = 0;
                        InputStream is = null;
                        FileOutputStream fos = null;
                        try {
                            //执行请求成功的操作
                            byte[] buf = new byte[2048];
                            int len;
                            is = responseBody.byteStream();
                            File file = new File(savePath);
                            fos = new FileOutputStream(file);
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                hasRead = hasRead + len;
                                dialog.setProgress((int) (hasRead / responseBody.contentLength() * 100));
                            }
                            //下载完成文件的写入、dialog的处理和打开文件
                            fos.flush();
                            dialog.dismiss();
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            install.setDataAndType(Uri.fromFile(new File(savePath)), "application/vnd.android.package-archive");
                            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(install);
                        } catch (IOException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        } finally {
                            try {
                                if (is != null) {
                                    is.close();
                                    dialog.dismiss();
                                }
                                if (fos != null) {
                                    fos.close();
                                    dialog.dismiss();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }
                    }
                });
    }

}
