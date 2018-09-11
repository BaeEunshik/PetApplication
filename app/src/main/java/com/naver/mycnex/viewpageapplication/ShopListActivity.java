package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.naver.mycnex.viewpageapplication.adapter.ShopListActListAdapter;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreData;
import com.naver.mycnex.viewpageapplication.login.LoginService;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShopListActivity extends AppCompatActivity {

    // 리스트뷰 관련
    ArrayList<StoreData> storeData;
    ShopListActListAdapter shopListActListAdapter;

    private Unbinder unbinder;

    @BindView(R.id.btnAdd) Button btnAdd;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;
    @BindView(R.id.searchStore_gv) GridView searchStore_gv;

    /** OnCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        //버터나이프
        unbinder = ButterKnife.bind(this);
        initWhenCreated();


    }

    /********** OnClick **********/
    @OnClick(R.id.btnAdd)
    public void btnAdd(){
        Intent intent = new Intent(ShopListActivity.this, RegisterShopActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnGoBack)
    public void btnGoBack(){
        finish();
    }

    /** OnDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //버터나이프
        unbinder.unbind();
    }

    public void initWhenCreated() {
        getMyStore();
    }

    public void getMyStore() {
        LoginService loginService = LoginService.getInstance();
        long member_id = loginService.getLoginMember().getId();

        Call<ArrayList<StoreData>> getMyStore = RetrofitService.getInstance().getRetrofitRequest().getMyStore(member_id);
        getMyStore.enqueue(new Callback<ArrayList<StoreData>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreData>> call, Response<ArrayList<StoreData>> response) {
                if (response.isSuccessful()) {
                    storeData = response.body();
                    initAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StoreData>> call, Throwable t) {

            }
        });
    }

    public void initAdapter() {
        shopListActListAdapter = new ShopListActListAdapter(storeData);
        searchStore_gv.setAdapter(shopListActListAdapter);
    }

}
