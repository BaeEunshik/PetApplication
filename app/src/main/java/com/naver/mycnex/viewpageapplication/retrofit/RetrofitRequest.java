package com.naver.mycnex.viewpageapplication.retrofit;

import com.naver.mycnex.viewpageapplication.data.Mark;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreImage;

import java.lang.reflect.Array;
import java.util.ArrayList;

import lombok.Getter;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018-01-29.
 */

public interface RetrofitRequest {

    @FormUrlEncoded
    @POST("join.do")
    Call<Boolean> joinMember(@Field("login_id") String login_id, @Field("login_pw") String login_pw, @Field("name") String name);

    @FormUrlEncoded
    @POST("login.do")
    Call<Boolean> login(@Field("login_id") String login_id, @Field("login_pw") String login_pw);

    @Multipart
    @POST("submitStore.do")
    Call<Long> submitStore(@Part ArrayList<MultipartBody.Part> storeImage, @Part MultipartBody.Part name, @Part MultipartBody.Part contact, @Part MultipartBody.Part dog_size, @Part MultipartBody.Part info,
                           @Part MultipartBody.Part operation_day, @Part MultipartBody.Part operation_time, @Part MultipartBody.Part parking, @Part MultipartBody.Part reservation,
                           @Part MultipartBody.Part address, @Part MultipartBody.Part sigungu, @Part MultipartBody.Part lat, @Part MultipartBody.Part lng, @Part MultipartBody.Part category);

    @FormUrlEncoded
    @POST("storeDetail.do")
    Call<StoreImage> storeDetail(@Field("id")long id);

    @GET("getStoreGeneral.do")
    Call<ArrayList<Store>> getStoreGeneral();

    @GET("getStoreSpecial.do")
    Call<ArrayList<Store>> getStoreSpecial();

    @GET("getStoreForMap.do")
    Call<ArrayList<Store>> getStoreForMap();

}
