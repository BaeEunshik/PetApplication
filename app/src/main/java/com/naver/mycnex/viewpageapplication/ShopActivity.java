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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.mycnex.viewpageapplication.adapter.ShopActRecyclerAdapter;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.TESTImage;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// TODO :

// 1) 이미지 클릭 ( 상단 )
//  - 스택 방식의 이미지 스크롤 ( 리사이클러 뷰 ) 구현 ( o )
//  - PhotosActivity / Adapter 생성 및 스크롤 내부 버튼 onclick 연결 ( o )
//      - PhotosActivity 스크롤 내부 사진 onclick 연결 ( o )
//  - PhotoActivity 생성 및 사진 onclick 연결 ( o )

// 3) 리뷰 작업
//  - ReviewWriteActivity 생성 및 onclick 연결
//  - 리뷰아이템 ( 미화님 작업 ) 수정 및 재사용

// 4) ShopOnMapActivity 작업


public class ShopActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    //전화번호
    public static String PHONE_NUMBER = "";
    private GoogleMap mMap;

    //버터나이프
    Unbinder unbinder;

    @BindView(R.id.reservation_txt) TextView reservation_txt;
    @BindView(R.id.parking_txt) TextView parking_txt;
    @BindView(R.id.dogSize_txt) TextView dogSize_txt;
    @BindView(R.id.operate_day_txt) TextView operate_day_txt;
    @BindView(R.id.operate_time_txt) TextView operate_time_txt;
    @BindView(R.id.textAddress) TextView textAddress;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;
    @BindView(R.id.btnGoReviewWrite)Button btnGoReviewWrite;
    @BindView(R.id.btnCall)Button btnCall;
    @BindView(R.id.horizonRecyclerView)RecyclerView horizonRecyclerView;
    @BindView(R.id.storeName_txt) TextView storeName_txt;

    //리사이클뷰
    ShopActRecyclerAdapter shopActRecyclerAdapter;
    ArrayList<TESTImage> testImages = new ArrayList<>();

    Store store;

    /** OnCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        // 버터나이프
        unbinder = ButterKnife.bind(this);
        initWhenCreated();

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
        PHONE_NUMBER = store.getContact();
        CallPermission();
    }
    @OnClick(R.id.btnGoReviewWrite)// 리뷰작성 하러 가기
    public void btnGoReviewWrite(){
        Intent intent = new Intent(ShopActivity.this,ReviewWriteActivity.class);
        startActivity(intent);
    }


    /******************** GoogleMap Fragment Interface Method ******************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapPreventEvent(googleMap);
        MapOnClick(googleMap);
        ZoomMap(googleMap);
    }

    public void ZoomMap(GoogleMap googleMap) {
        // 맵 마커, 줌 설정
        mMap = googleMap;
        LatLng storeLocation = new LatLng(store.getLatitude(), store.getLongitude());
        mMap.addMarker(new MarkerOptions().position(storeLocation).title(store.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation,16)); // 16은 zoom-in level

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void MapPreventEvent(GoogleMap googleMap) {
        // 이벤트 방지
        googleMap.getUiSettings().setRotateGesturesEnabled(false);  // Rotate false
        googleMap.getUiSettings().setZoomGesturesEnabled(false);    // Zoom false
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);    // Tilt false
        googleMap.getUiSettings().setScrollGesturesEnabled(false);  // Scroll false
        googleMap.getUiSettings().setMapToolbarEnabled(false);      // 클릭시 나오는 툴바 제거
    }

    public void MapOnClick(GoogleMap googleMap) {
        // 맵 클릭 시
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(ShopActivity.this,ShopOnMapActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initWhenCreated() {
        getIddata();
        recycleViewSet();
        // 리사이클뷰 임시 데이터 삽입 ***
        // TODO :
        // 바꿔야 함
        TESTImage imgObj = new TESTImage(R.drawable.dog1);
        testImages.add(imgObj);
        testImages.add(imgObj);
        testImages.add(imgObj);
        testImages.add(imgObj);
        testImages.add(imgObj);


    }

    public void CallPermission() {
        // 권한
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 체크
                if (ActivityCompat.checkSelfPermission(ShopActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                // 전화
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PHONE_NUMBER));
                startActivity(intent);
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

    public void googleMapComponent() {
        // 구글 맵 컴포넌트
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void getIddata() {

        Intent intent = getIntent();
        Long id = intent.getLongExtra("id",-1);

        Call<Store> getStoreData = RetrofitService.getInstance().getRetrofitRequest().storeDetail(id);
        getStoreData.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                if (response.isSuccessful()) {
                    store = response.body();
                    getDataFromId();
                }
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getDataFromId() {
        googleMapComponent();
        storeName_txt.setText(store.getName());
        textAddress.setText(store.getAddress());
        operate_time_txt.setText(store.getOperation_time());
        operate_day_txt.setText(store.getOperation_day());
        if (store.getDog_size() == RegisterShopActivity.PETSIZE_SMALL) {
            dogSize_txt.setText("소형견");
        } else if (store.getDog_size() == RegisterShopActivity.PETSIZE_MIDIUM) {
            dogSize_txt.setText("중형견");
        } else if (store.getDog_size() == RegisterShopActivity.PETSIZE_LARGE) {
            dogSize_txt.setText("대형견");
        }
        if (store.getParking() == RegisterShopActivity.PARKING_ABLE) {
            parking_txt.setText("주차가능");
        } else if (store.getParking() == RegisterShopActivity.PARKING_UNABLE) {
            parking_txt.setText("주차불가");
        } else if (store.getParking() == RegisterShopActivity.PARKING_VALET) {
            parking_txt.setText("발렛주차");
        }
        if (store.getReservation() == RegisterShopActivity.RESERVATION_ABLE) {
            reservation_txt.setText("예약가능");
        } else if (store.getReservation() == RegisterShopActivity.RESERVATION_UNABLE) {
            reservation_txt.setText("예약불가");
        }
    }

    public void recycleViewSet() {
        // 리사이클뷰 set
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        horizonRecyclerView.setLayoutManager(layoutManager);
        shopActRecyclerAdapter = new ShopActRecyclerAdapter(testImages, getApplicationContext());
        horizonRecyclerView.setAdapter(shopActRecyclerAdapter);
    }
}
