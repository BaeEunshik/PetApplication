package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ShopListActivity extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.btnAdd) Button btnAdd;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;

    /** OnCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        //버터나이프
        unbinder = ButterKnife.bind(this);

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
}
