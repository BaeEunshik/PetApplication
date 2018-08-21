package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;


public class ShopListActivity extends AppCompatActivity {

//2018.08.21
// 15:23
// 배은식

    @BindView(R.id.ibtn_add)
    Button ibtn_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);



        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopListActivity.this, RegisterShopActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
