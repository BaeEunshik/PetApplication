package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.naver.mycnex.viewpageapplication.adapter.ShopListListAdapter;
import com.naver.mycnex.viewpageapplication.data.Store;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;


public class ShopListActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.btnAdd) Button btnAdd;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;
    @BindView(R.id.listView) ListView listView;

    // 리스트뷰 관련
    ArrayList<Store> storeList = new ArrayList<>();
    ShopListListAdapter shopListListAdapter;

    /** OnCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        //버터나이프
        unbinder = ButterKnife.bind(this);

        // 임시 객체리스트 생성
        storeList.add(
                new Store(1,"NAME_1",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강남구","논현동",10.00,20.00,1
                ));
        storeList.add(
                new Store(2,"NAME_2",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강동구","둔촌동",10.00,20.00,1
                ));
        storeList.add(
                new Store(3,"NAME_3",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강서구","무슨동",10.00,20.00,1
                ));
        storeList.add(
                new Store(4,"NAME_4",000,2,"information","00:00","24:00",
                2,1,"address","서울시 강북구","어떤동",10.00,20.00,1
        ));
        storeList.add(
                new Store(5,"NAME_5",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 송파구","이런동",10.00,20.00,1
                ));


        shopListListAdapter = new ShopListListAdapter(storeList);
        listView.setAdapter(shopListListAdapter);
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

    /********** OnItemClick **********/
    @OnItemClick(R.id.listView)
    public void listView(){
        // TODO : 클릭한

    }
}
