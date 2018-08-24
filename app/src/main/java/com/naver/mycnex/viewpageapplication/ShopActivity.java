package com.naver.mycnex.viewpageapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.mycnex.viewpageapplication.adapter.ShopActRecyclerAdapter;
import com.naver.mycnex.viewpageapplication.data.TESTImage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


// TODO :
// 1) 전화하기
//  - 기능
//  - 전화기 이미지 넣기

// 2) 이미지 클릭
//    " 상단 스크롤뷰의 이미지 클릭하면 사진액티비티 이동 "
//  - 스택 방식의 이미지 스크롤뷰 구현
//  - PhotoActivity 생성 및 onclick 연결

// 3) 리뷰 작업
//  - ReviewWriteActivity 생성 및 onclick 연결
//  - 리뷰아이템 ( 미화님 작업 ) 수정 및 재사용

// 4) ShopOnMapActivity 작업


public class ShopActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    //전화번호
    public static String PHONE_NUMBER = "";

    //버터나이프
    private Unbinder unbinder;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;
    @BindView(R.id.btnGoWriteReview) Button btnGoWriteReview;
    @BindView(R.id.btnCall) Button btnCall;
    @BindView(R.id.horizonRecyclerView) RecyclerView horizonRecyclerView;

    //리사이클뷰
    ShopActRecyclerAdapter shopActRecyclerAdapter;
    ArrayList<TESTImage> testImages = new ArrayList<>();

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

        // 리사이클뷰 임시 데이터 삽입
        TESTImage image = new TESTImage(R.drawable.dog1);
        testImages.add(image);
        testImages.add(image);
        testImages.add(image);
        testImages.add(image);
        testImages.add(image);

        Log.d("은식",Integer.toString(testImages.size()));

        // 리사이클뷰 set
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizonRecyclerView.setLayoutManager(layoutManager);
        shopActRecyclerAdapter = new ShopActRecyclerAdapter(testImages,getApplicationContext());
        horizonRecyclerView.setAdapter(shopActRecyclerAdapter);
    }
    /** OnDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 버터나이프
        unbinder.unbind();
    }

    /****************************** OnClick ******************************/

    @OnClick(R.id.btnGoBack)// 뒤로가기
    public void setBtnGoBack() {
        finish();
    }
    @OnClick(R.id.btnCall)// 전화하기
    public void btnCall() {
        // 전화번호
        PHONE_NUMBER = "01026825414";
        // 권한
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PHONE_NUMBER));
                if (ActivityCompat.checkSelfPermission(ShopActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        TedPermission.with(ShopActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();
    }
    @OnClick(R.id.btnGoWriteReview)// 리뷰작성 하러 가기
    public void btnGoWriteReview(){
        //TODO
    }


    /******************** GoogleMap Fragment Interface Method ******************/
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
