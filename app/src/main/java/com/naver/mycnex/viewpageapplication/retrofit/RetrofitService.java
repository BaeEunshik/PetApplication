package com.naver.mycnex.viewpageapplication.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018-01-29.
 */

public class RetrofitService {
    public static RetrofitService curr = null;
    private RetrofitRequest retrofitRequest;

    public static RetrofitService getInstance() {
        if (curr == null) {
            curr = new RetrofitService();
        }

        return curr;
    }

    private RetrofitService() {
        retrofitRequest = init();
    }

    public RetrofitRequest init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.61:8090/petApplication/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitRequest.class);
    }

    public RetrofitRequest getRetrofitRequest() {
        return retrofitRequest;
    }
}
