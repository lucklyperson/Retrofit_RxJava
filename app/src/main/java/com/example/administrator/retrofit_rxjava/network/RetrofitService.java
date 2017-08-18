package com.example.administrator.retrofit_rxjava.network;

import com.shaodianbao.entity.GoodsCost;
import com.shaodianbao.entity.RedPacket;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by $wu on 2017-08-03 下午 2:57.
 * Retrofit请求的接口
 */

public interface RetrofitService {


    //apk的下载
    @GET
    @Streaming
    Observable<ResponseBody> apk_download(@Url String url);

    //抽红包的接口
    @FormUrlEncoded
    @POST("api.php?con=user&act=chou")
    Observable<RedPacket> grab_redPacket(@Field("id") String id, @Field("auth") String auth);

    //捎物订单基础价格
    @FormUrlEncoded
    @POST("api.php?con=price&act=basePrice")
    Observable<GoodsCost> goods_basePrice(@FieldMap() Map<String, String> map);

    //更改头像
    @Multipart
    @POST("api.php?con=user&act=updateavatar")
    Observable<ResponseBody> upload_avatar(@Part MultipartBody.Part part, @Part("auth") RequestBody paramsBody);

    //更改头像
    @POST("api.php?con=user&act=updateavatar")
    Observable<ResponseBody> upload_avatar(@Body RequestBody body);

    //上传司机认证的消息
    @Multipart
    @POST("api.php?con=user&act=drvierCertify")
    Observable<ResponseBody> driver_auth(@Part List<MultipartBody.Part> parts, @PartMap Map<String, RequestBody> params);

    //上传司机认证的消息
    @POST("api.php?con=user&act=drvierCertify")
    Observable<ResponseBody> driver_auth(@Body RequestBody body);


}

