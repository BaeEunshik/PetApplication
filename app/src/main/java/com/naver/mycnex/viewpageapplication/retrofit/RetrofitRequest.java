package com.naver.mycnex.viewpageapplication.retrofit;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018-01-29.
 */

public interface RetrofitRequest {

    /* POST 형식
    @FormUrlEncoded
    @POST("write_json_ok.do")
    Call<Void> writeMemo(@Field("content") String content);
    */

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
