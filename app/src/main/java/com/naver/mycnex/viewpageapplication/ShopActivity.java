package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShopActivity extends AppCompatActivity {

    //버터나이프
    private Unbinder unbinder;

    @BindView(R.id.btnGoBack) ImageButton btnGoBack;

    /** OnCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        // 버터나이프
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 버터나이프
        unbinder.unbind();
    }

    /********** OnClick **********/
    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){     //뒤로가기
        finish();
    }
}
