package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.naver.mycnex.viewpageapplication.adapter.PhotosActGridAdapter;
import com.naver.mycnex.viewpageapplication.data.TESTImage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PhotosActivity extends AppCompatActivity {

    //버터나이프
    private Unbinder unbinder;
    @BindView(R.id.gridView) GridView gridView;

    ArrayList<TESTImage> testImages = new ArrayList<>();
    PhotosActGridAdapter photosActGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        unbinder = ButterKnife.bind(this);

        // 그리드뷰 임시 데이터 삽입 ***
        // TODO :
        // 바꿔야 함
        TESTImage imgObj = new TESTImage(R.drawable.dog1);
        testImages.add(imgObj);
        testImages.add(imgObj);
        testImages.add(imgObj);
        testImages.add(imgObj);
        testImages.add(imgObj);

        // 어댑터 set
        photosActGridAdapter = new PhotosActGridAdapter(testImages);
        gridView.setAdapter(photosActGridAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //버터나이프
        unbinder.unbind();
    }
}
