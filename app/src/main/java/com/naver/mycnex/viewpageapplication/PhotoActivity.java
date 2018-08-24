package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.naver.mycnex.viewpageapplication.glide.GlideApp;

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
        Integer imgData = intent.getIntExtra("imgData",0);
        img.setImageResource(imgData);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
