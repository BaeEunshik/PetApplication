package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.adapter.PhotosActGridAdapter;
import com.naver.mycnex.viewpageapplication.data.ImageFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PhotosActivity extends AppCompatActivity {

    //버터나이프
    private Unbinder unbinder;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.gridView) GridView gridView;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;


    ArrayList<ImageFile> testImages;
    PhotosActGridAdapter photosActGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        unbinder = ButterKnife.bind(this);

        // 그리드뷰 임시 데이터 삽입 ***
        // TODO :
        // 바꿔야 함

        // 서버에서 데이터 가져오기

        // 가게 ( 장소 ) 이름 set
        title.setText("가게이름(객체연동필요)");

        // 어댑터 set

        testImages = new ArrayList<>();
        photosActGridAdapter = new PhotosActGridAdapter(testImages);
        gridView.setAdapter(photosActGridAdapter);
    }

    @OnClick(R.id.btnGoBack)
    public void btnGoBack(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //버터나이프
        unbinder.unbind();
    }
}
