package com.naver.mycnex.viewpageapplication;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


// TODO :
// 1) 전화하기
//  - 기능
//  - 전화기 이미지 넣기

// 2) 리뷰 작업
//  - ReviewWriteActivity 생성 및 onclick 연결
//  - 리뷰아이템 ( 미화님 ) 수정 및 재사용

// 3) 이미지 클릭
//    " 상단 스크롤뷰의 이미지 클릭하면 사진액티비티 이동 "
//  - 스택 방식의 이미지 스크롤뷰 구현
//  - PhotoActivity 생성 및 onclick 연결


public class ShopActivity extends AppCompatActivity
        implements OnMapReadyCallback{

    //버터나이프
    private Unbinder unbinder;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;
    @BindView(R.id.btnGoWriteReview) ImageButton btnGoWriteReview;

    /** OnCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        // 버터나이프
        unbinder = ButterKnife.bind(this);

        // 구글 맵 컴포넌트
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    /** OnDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 버터나이프
        unbinder.unbind();
    }

    /********** OnClick **********/
    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){     // 뒤로가기
        finish();
    }
    @OnClick(R.id.btnGoWriteReview)
    public void btnGoWriteReview(){     // 리뷰작성 하러 가기
        //TODO
    }

    /********** GoogleMap Fragment Interface Method ********/
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // 이벤트 방지
        googleMap.getUiSettings().setAllGesturesEnabled(false); // 모든 제스처 막기
        googleMap.getUiSettings().setMapToolbarEnabled(false);  // 클릭시 나오는 툴바 제거

        // 맵 클릭 시
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(ShopActivity.this,ShopOnMapActivity.class);
                startActivity(intent);
            }
        });
        // 맵 마커 설정
        LatLng location = new LatLng(37.52487,126.92723);//여의도
        googleMap.addMarker(
                new MarkerOptions()
                .position(location)
                .title("Marker")
        );
        // 카메라 위치
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));

    }
}
