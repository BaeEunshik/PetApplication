package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PhotoActivity extends AppCompatActivity {

    private Unbinder unbinder;
    @BindView(R.id.img) ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        Integer imgData = intent.getIntExtra("imgData",-1);
        // 이미지 아이디 데이터베이스에서 받아오기 ㅏㄴ머아ㅣ먼아ㅣ먼애ㅏㅣ머놔이ㅓㅁ자ㅣㅓ이마ㅓㄴ아ㅣ
        img.setImageResource(imgData);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
