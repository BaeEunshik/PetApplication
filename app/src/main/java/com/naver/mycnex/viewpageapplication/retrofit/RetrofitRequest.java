package com.naver.mycnex.viewpageapplication.retrofit;

import com.naver.mycnex.viewpageapplication.data.Member;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
