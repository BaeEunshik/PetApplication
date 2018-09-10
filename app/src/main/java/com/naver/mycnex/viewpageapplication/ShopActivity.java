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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.mycnex.viewpageapplication.adapter.ReviewListAdapter;
import com.naver.mycnex.viewpageapplication.adapter.ShopActRecyclerAdapter;
import com.naver.mycnex.viewpageapplication.data.ImageFile;
import com.naver.mycnex.viewpageapplication.data.Member;
import com.naver.mycnex.viewpageapplication.data.Review;
import com.naver.mycnex.viewpageapplication.data.ReviewMember;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreData;
import com.naver.mycnex.viewpageapplication.data.StoreImage;
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.login.LoginService;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// TODO :

// 3) 리뷰 작업
//  - ReviewWriteActivity 생성 및 onclick 연결
//  - 리뷰아이템 ( 미화님 작업 ) 수정 및 재사용

// 4) ShopOnMapActivity 작업


public class ShopActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    //전화번호
    private GoogleMap mMap;

    private int LOGIN_SUCCESS = 1;
    private int WRITE_FINISH = 2;
    ReviewListAdapter reviewListAdapter;

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
    @BindView(R.id.View_txt) TextView View_txt;
    @BindView(R.id.review_lv) ListView review_lv;
    @BindView(R.id.storeScore_txt) TextView storeScore_txt;
    @BindView(R.id.reviewSize_txt) TextView reviewSize_txt;
    @BindView(R.id.review_count_txt) TextView review_count_txt;

    //리사이클뷰
    ShopActRecyclerAdapter shopActRecyclerAdapter;

    StoreData storeData;
    Store store;
    ArrayList<ImageFile> images;
    ArrayList<Review> reviews;
    ArrayList<Member> members;
    ArrayList<ReviewMember> reviewMembers;

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
        String phoneNum = store.getContact();
        CallPermission(phoneNum);
    }
    @OnClick(R.id.btnGoReviewWrite)// 리뷰작성 하러 가기
    public void btnGoReviewWrite(){

        LoginService loginService = LoginService.getInstance();

        if (loginService.getLoginMember() == null) {
            Toast.makeText(this,"로그인이 필요합니다",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ShopActivity.this,LoginActivity.class);
            intent.putExtra("review",1);
            startActivityForResult(intent,LOGIN_SUCCESS);
        } else {
            Intent intent = new Intent(ShopActivity.this,ReviewWriteActivity.class);
            intent.putExtra("store_id",store.getId());
            startActivityForResult(intent,WRITE_FINISH);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_SUCCESS) {
            if (resultCode == RESULT_OK) {
                // review 데이터 뿌려주기
            }
        } else if (requestCode == WRITE_FINISH) {
            if (resultCode == RESULT_OK) {
                getIdData();
            }
        }

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
        getIdData();
    }

    public void CallPermission(final String phoneNum) {
        // 권한
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 체크
                if (ActivityCompat.checkSelfPermission(ShopActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                // 전화
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
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

    public void getIdData() {

        Intent intent = getIntent();
        Long id = intent.getLongExtra("id",-1);

        Call<StoreData> getStoreData = RetrofitService.getInstance().getRetrofitRequest().storeDetail(id);
        getStoreData.enqueue(new Callback<StoreData>() {
            @Override
            public void onResponse(Call<StoreData> call, Response<StoreData> response) {
                if (response.isSuccessful()) {
                    storeData = response.body();
                    getDataFromId();
                }
            }

            @Override
            public void onFailure(Call<StoreData> call, Throwable t) {

            }
        });
    }

    public void getDataFromId() {

        images = storeData.getImages();
        store = storeData.getStore();

        recycleViewSet();

        googleMapComponent();
        storeName_txt.setText(store.getName());
        textAddress.setText(store.getAddress());
        operate_time_txt.setText(store.getOperation_time());
        operate_day_txt.setText(store.getOperation_day());
        if (store.getDog_size() == Global.PETSIZE_SMALL) {
            dogSize_txt.setText("소형견");
        } else if (store.getDog_size() == Global.PETSIZE_MIDIUM) {
            dogSize_txt.setText("중형견");
        } else if (store.getDog_size() == Global.PETSIZE_LARGE) {
            dogSize_txt.setText("대형견");
        }
        if (store.getParking() == Global.PARKING_ABLE) {
            parking_txt.setText("주차가능");
        } else if (store.getParking() == Global.PARKING_UNABLE) {
            parking_txt.setText("주차불가");
        } else if (store.getParking() == Global.PARKING_VALET) {
            parking_txt.setText("발렛주차");
        }
        if (store.getReservation() == Global.RESERVATION_ABLE) {
            reservation_txt.setText("예약가능");
        } else if (store.getReservation() == Global.RESERVATION_UNABLE) {
            reservation_txt.setText("예약불가");
        }

        double result = ((double)store.getScore_sum())/((double)store.getScore_count());
        double getPrimeNum = Math.ceil(result*10d) / 10d;

        if (Double.isNaN(getPrimeNum)) {
            storeScore_txt.setText("0.0");
        } else {
            storeScore_txt.setText(String.valueOf(getPrimeNum));
        }

        View_txt.setText(store.getHit().toString());
        getReviewMemberData();
        initReviewAdatper();
    }

    public void recycleViewSet() {
        // 리사이클뷰 set
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        horizonRecyclerView.setLayoutManager(layoutManager);
        shopActRecyclerAdapter = new ShopActRecyclerAdapter(images, getApplicationContext());
        horizonRecyclerView.setAdapter(shopActRecyclerAdapter);
    }

    public void getReviewMemberData() {

        reviews = new ArrayList<>();
        members = new ArrayList<>();

        reviewSize_txt.setText(String.valueOf(storeData.getReviews().size()));
        review_count_txt.setText("리뷰 (" + String.valueOf(storeData.getReviews().size()) + ")");

        for (int i = 0; i < storeData.getReviews().size(); i++) {
            reviews.add(storeData.getReviews().get(i));
            members.add(storeData.getMembers().get(i));
        }
    }

    public void initReviewAdatper() {
        reviewListAdapter = new ReviewListAdapter(reviews,members);
        review_lv.setAdapter(reviewListAdapter);
        setListViewHeightBasedOnChildren(review_lv);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
