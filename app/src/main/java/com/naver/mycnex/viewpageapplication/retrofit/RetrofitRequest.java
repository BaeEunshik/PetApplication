package com.naver.mycnex.viewpageapplication.retrofit;

import com.naver.mycnex.viewpageapplication.data.Store;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

    @FormUrlEncoded
    @POST("submitStore.do")
    Call<Long> submitStore(@Field("name")String name, @Field("contact") String contact, @Field("dog_size") Integer dog_size, @Field("store_information")String info,
                           @Field("operation_day")String operation_day, @Field("operation_time") String operation_time, @Field("parking") Integer parking, @Field("reservation") Integer reservation,
                           @Field("address")String address, @Field("sigungu")String sigungu, @Field("dong") String dong, @Field("lat") double lat, @Field("lng") double lng, @Field("category") Integer category);

    @FormUrlEncoded
    @POST("storeDetail.do")
    Call<Store> storeDetail(@Field("id")long id);

    @GET("getStoreData.do")
    Call<ArrayList<Store>> getStoreData();

    /* 파일 업로드
    @Multipart
    @POST("write_json_ok.do")
    Call<Void> writeMemo(@Part("content") RequestBody content, @Part MultipartBody.Part photo);
    */

    /* GET 형식
    @GET("detail_json.do")
    Call<MemoFiles> getMemoFileList(@Query("id") Long id);
    */

}
