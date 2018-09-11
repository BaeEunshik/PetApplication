package com.naver.mycnex.viewpageapplication.retrofit;

import com.naver.mycnex.viewpageapplication.data.Member;
import com.naver.mycnex.viewpageapplication.data.Review;
import com.naver.mycnex.viewpageapplication.data.ReviewMember;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreData;
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
    Call<Member> login(@Field("login_id") String login_id, @Field("login_pw") String login_pw);

    @Multipart
    @POST("submitStore.do")
    Call<Long> submitStore(@Part ArrayList<MultipartBody.Part> storeImage, @Part MultipartBody.Part name, @Part MultipartBody.Part contact, @Part MultipartBody.Part dog_size, @Part MultipartBody.Part info,
                           @Part MultipartBody.Part operation_day, @Part MultipartBody.Part operation_time, @Part MultipartBody.Part parking, @Part MultipartBody.Part reservation,
                           @Part MultipartBody.Part address, @Part MultipartBody.Part sigungu, @Part MultipartBody.Part lat, @Part MultipartBody.Part lng, @Part MultipartBody.Part category, @Part MultipartBody.Part member_id);

    @FormUrlEncoded
    @POST("storeDetail.do")
    Call<StoreData> storeDetail(@Field("id")long id);

    @FormUrlEncoded
    @POST("getStoreGeneral.do")
    Call<ArrayList<StoreData>> getStoreGeneral(@Field("sigungu") Integer sigungu, @Field("dog_size") Integer dog_size, @Field("category") Integer category);

    @FormUrlEncoded
    @POST("getStoreGeneralWhenLogIn.do")
    Call<ArrayList<StoreData>> getStoreGeneralWhenLogIn(@Field("sigungu") Integer sigungu, @Field("dog_size") Integer dog_size, @Field("category") Integer category, @Field("member_id") long member_id);

    @FormUrlEncoded
    @POST("getStoreSpecial.do")
    Call<ArrayList<StoreData>> getStoreSpecial(@Field("sigungu") Integer sigungu, @Field("dog_size") Integer dog_size, @Field("category") Integer category);

    @FormUrlEncoded
    @POST("getStoreSpecialWhenLogIn.do")
    Call<ArrayList<StoreData>> getStoreSpecialWhenLogIn(@Field("sigungu") Integer sigungu, @Field("dog_size") Integer dog_size, @Field("category") Integer category, @Field("member_id") long member_id);

    @GET("getStoreForMap.do")
    Call<ArrayList<StoreImage>> getStoreForMap();

    @FormUrlEncoded
    @POST("WriteReview.do")
    Call<Void> WriteReview(@Field("content") String content, @Field("score") String score, @Field("store_id") long store_id, @Field("member_id") long member_id, @Field("date") String date);

    @GET("GetStoreForSearch.do")
    Call<ArrayList<StoreData>> GetStoreForSearch();

    @GET("AddBookMark.do")
    Call<Void> AddBookMark(@Query("store_id") long store_id, @Query("member_id") long member_id);

    @GET("DeleteBookMark.do")
    Call<Void> DeleteBookMark(@Query("store_id") long store_id, @Query("member_id") long member_id);

    @FormUrlEncoded
    @POST("getBookmarkStore.do")
    Call<ArrayList<StoreData>> getBookmarkStore(@Field("member_id")long member_id);

    @FormUrlEncoded
    @POST("getMyStore.do")
    Call<ArrayList<StoreData>> getMyStore(@Field("member_id") long member_id);

}
