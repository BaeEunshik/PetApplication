package com.naver.mycnex.viewpageapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.mycnex.viewpageapplication.data.ImageFile;
import com.naver.mycnex.viewpageapplication.data.Mark;
import com.naver.mycnex.viewpageapplication.data.Review;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreData;
import com.naver.mycnex.viewpageapplication.data.StoreImage;
import com.naver.mycnex.viewpageapplication.glide.GlideApp;
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.gps.GpsInfo;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.ToString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMapActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //구글맵
    private GoogleMap mMap;
    double lat;  //위도
    double lng; //경도

    //store 정보
    ArrayList<StoreData> storeData;
    ArrayList<Store> stores;
    ArrayList<ImageFile> images;
    ArrayList<Integer> reviews;
    Store store; // 초기에 띄워줄 store 정보. ( 현재 위치와 가장 밀접한 매장 )

    //버터나이프
    Unbinder unbinder;
    @BindView(R.id.btnSrchText) ImageButton btnSrchText;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;
    @BindView(R.id.spinnerLocate) Spinner spinnerLocate;
    @BindView(R.id.spinnerPurpose) Spinner spinnerPurpose;
    @BindView(R.id.spinnerPlace) Spinner spinnerPlace;
    @BindView(R.id.storeContainer) RelativeLayout storeContainer;
    @BindView(R.id.storeimage_img) ImageView storeimage_img;
    @BindView(R.id.storeName_txt) TextView storeName_txt;
    @BindView(R.id.storeCategory_txt) TextView storeCategory_txt;
    @BindView(R.id.storeLocation_txt) TextView storeLocation_txt;
    @BindView(R.id.dog_size_txt) TextView dog_size_txt;
    @BindView(R.id.View_txt) TextView View_txt;
    @BindView(R.id.review_count_txt) TextView review_count_txt;

    // OnCreate ( 최초실행 ) 시 중복 BUS 실행 차단 위한 Flag
    private boolean LOCATION_FLAG = false;
    private boolean CATEGORY_FLAG = false;
    // SQL 보낼 Spinner 의 index
    private int LOCATION = 0;   // [ 전체 ]
    private int PURPOSE = 0;
    private int CATEGORY = 0;    // [ 전체 ]

    private int DISTANCE_BOUNDARY = 1000;
    private int DISTANCE_MIN = DISTANCE_BOUNDARY;

    // 카테고리 Spinner 생성에 사용할 배열 변수
    private static int DEFAULT_ITEM_IDX = 1; // "전체" 는 DB에 없기 때문에 index 에서 빼줘야 한다.
    private String[] CATEGORY_GENERAL_ARR = new String[Global.CATEGORY_GENERAL_LENGTH + DEFAULT_ITEM_IDX];
    private String[] CATEGORY_SPECIAL_ARR = new String[Global.CATEGORY_SPECIAL_LENGTH + DEFAULT_ITEM_IDX];

    /** onCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        //버터나이프
        unbinder = ButterKnife.bind(this);
        InitWhenCreated();

    }
    /** onDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //버터나이프
        unbinder.unbind();
    }

    /********** OnClick **********/
    @OnClick(R.id.btnSrchText)
    public void btnSrchText(){
        Intent intent = new Intent(SearchMapActivity.this,SearchKeywordActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnGoBack)
    public void btnGoBack(){
       finish();
    }

    /********** 맵 관련 **********/
    //OnMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //맵 화면 컴포넌트
        mMap = googleMap;
        enableMyLocation();
        getMarkCurrentLocation();
    }
    //onMyLocationClick
    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
    //onMyLocationButtonClick
    @Override
    public boolean onMyLocationButtonClick() {
        getCurrentLocation();
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        getMarkerInfo(marker);
        return false;
    }

    public void getMarkerInfo(Marker marker) {
        storeContainer.setVisibility(View.VISIBLE);

        Integer markerTag = (Integer) marker.getTag();

        if (markerTag != null) {

            storeName_txt.setText(stores.get((Integer)marker.getTag()).getName());

            storeLocation_txt.setText(getResources().getStringArray(R.array.address1)[stores.get((Integer)marker.getTag()).getSigungu()]);
            dog_size_txt.setText(Global.PETSIZE_STR_ARR[stores.get((Integer)marker.getTag()).getDog_size()-1]);

            if (stores.get((Integer)marker.getTag()).getCategory() < Global.CATEGORY_DIVISION_NUM) { // general 일 때
                storeCategory_txt.setText(Global.CATEGORY_GENERAL_STR_ARR[stores.get((Integer)marker.getTag()).getCategory()-1]);
            } else {
                storeCategory_txt.setText(Global.CATEGORY_SPECIAL_STR_ARR[stores.get((Integer)marker.getTag()).getCategory()-Global.CATEGORY_DIVISION_NUM-1]);
            }

            review_count_txt.setText(reviews.get((Integer)marker.getTag()).toString());
            View_txt.setText(stores.get((Integer)marker.getTag()).getHit().toString());

            for (int i = 0; i < images.size(); i++) {

                if (images.get(i).getStore_id() == stores.get((Integer)marker.getTag()).getId()) {

                    GlideApp.with(SearchMapActivity.this)
                            .load(Global.BASE_IMAGE_URL+images.get(i).getSavedName())
                            .centerCrop()
                            .into( storeimage_img );

                    /*
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap image = null;
                            try {
                                image = getBitmap(Global.BASE_IMAGE_URL+images.get(finalI).getSavedName());
                            }catch(Exception e) {

                            }finally {
                                if(image!=null) {
                                    final Bitmap finalImage = image;
                                    runOnUiThread(new Runnable() {
                                        @SuppressLint("NewApi")
                                        public void run() {
                                            storeimage_img.setImageBitmap(finalImage);
                                        }
                                    });
                                }
                            }
                        }
                    }).start();
                    */
                }
            }
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    /********** 권한 관련 **********/
    public void Permission() {
        PermissionListener permissionlistener = new PermissionListener() {
            //권한 획득시
            @Override
            public void onPermissionGranted() {
//                getMarkCurrentLocation();
            }
            //권한 거부시
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(SearchMapActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("GPS 허용을 거부하셨습니다. \n[설정] > [권한]에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
    /************************* Oncreated *************************/
    public void InitWhenCreated() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dropDownCategoryArrSet();
        dropDownMenuDefaultSet();
        spinnerSetOnItemSelect();
        Permission();
        storeContainer.setVisibility(View.INVISIBLE);
    }

    public void dropDownMenuDefaultSet() {
        //Spinner ( 드롭다운 메뉴 ) 관련설정
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.address1, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocate.setAdapter(addressAdapter);

        ArrayAdapter dogsizeAdapter = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item);
        dogsizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPurpose.setAdapter(dogsizeAdapter);
    }
    // Spinner Arr Setting
    public void dropDownCategoryArrSet(){
        // Spinner ( 드롭다운 메뉴 ) 에 사용될 카테고리의 배열값 할당
        // Static 배열 복사
        System.arraycopy( Global.CATEGORY_GENERAL_STR_ARR, 0, CATEGORY_GENERAL_ARR, 1, Global.CATEGORY_GENERAL_LENGTH );
        System.arraycopy( Global.CATEGORY_SPECIAL_STR_ARR, 0, CATEGORY_SPECIAL_ARR, 1, Global.CATEGORY_SPECIAL_LENGTH );
        // "전체" 항목 추가
        CATEGORY_GENERAL_ARR[0] = "전체";
        CATEGORY_SPECIAL_ARR[0] = "전체";
    }
    // Spinner OnItemSelect
    public void spinnerSetOnItemSelect() {

        // 지역 선택시
        spinnerLocate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( LOCATION_FLAG ){
                    LOCATION = position;
                    getMarkByLocation();
                } else {
                    LOCATION_FLAG = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 애견동반, 애견전용 선택시
        spinnerPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchPlaceSpinner(position);
                PURPOSE = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(CATEGORY_FLAG){
                    if(PURPOSE == Global.CATEGORY_GENERAL){
                        CATEGORY = position;                                // default : 0
                        getMarkByLocation();
                    } else if (PURPOSE == Global.CATEGORY_SPECIAL){
                        CATEGORY = position + Global.CATEGORY_DIVISION_NUM; // default : 100
                        getMarkByLocation();
                    }
                }else{
                    CATEGORY_FLAG = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void switchPlaceSpinner(int index){
        if (index == Global.CATEGORY_GENERAL){
            ArrayAdapter generalAdapter = new ArrayAdapter(SearchMapActivity.this, android.R.layout.simple_spinner_item, CATEGORY_GENERAL_ARR);
            generalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlace.setAdapter(generalAdapter);
        } else if (index == Global.CATEGORY_SPECIAL){
            ArrayAdapter generalAdapter = new ArrayAdapter(SearchMapActivity.this, android.R.layout.simple_spinner_item, CATEGORY_SPECIAL_ARR);
            generalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlace.setAdapter(generalAdapter);
        }
    }

    public void getCurrentLocation() {

        LatLng currentLocation = getCurrentLocationByGPS();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        SetOnClickMyLocationButton();
    }

    public void SetOnClickMyLocationButton() {

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LatLng currentLocation = getCurrentLocationByGPS();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
                return false;
            }
        });
    }

    public LatLng getCurrentLocationByGPS() {
        GpsInfo gps = new GpsInfo(SearchMapActivity.this);

        if (gps.isGetLocation()) {

            lat = gps.getLatitude();
            lng = gps.getLongitude();

        }

        LatLng currentLocation = new LatLng(lat, lng);
        return currentLocation;
    }

    public void getMarkCurrentLocation() {
       Call<ArrayList<StoreData>> getstore = RetrofitService.getInstance().getRetrofitRequest().getStoreForMap();
        getstore.enqueue(new Callback<ArrayList<StoreData>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreData>> call, Response<ArrayList<StoreData>> response) {
                if (response.isSuccessful()) {
                    storeData = response.body();
                    getStoreData();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<StoreData>> call, Throwable t) {

            }
        });
    }

    public void getMarkByLocation() {
        Call<ArrayList<StoreData>> getStore = RetrofitService.getInstance().getRetrofitRequest().getStoreByCategory(LOCATION, CATEGORY);
        getStore.enqueue(new Callback<ArrayList<StoreData>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreData>> call, Response<ArrayList<StoreData>> response) {
                if (response.isSuccessful()) {
                    storeData = response.body();
                    getStoreData();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StoreData>> call, Throwable t) {

            }
        });
    }

    public void getStoreData() {

        mMap.clear();

        stores = new ArrayList<>();
        images = new ArrayList<>();
        reviews = new ArrayList<>();

        if (storeData.size() == 0) {
            storeContainer.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < storeData.size(); i++) {

            stores.add(storeData.get(i).getStore());
            images.add(storeData.get(i).getImages().get(0));
            reviews.add(storeData.get(i).getReviews().size());

            LatLng MarkerLocation = new LatLng(storeData.get(i).getStore().getLatitude(), storeData.get(i).getStore().getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(MarkerLocation)
                         .title(storeData.get(i).getStore().getName());

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(i);

            store = storeData.get(0).getStore();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(store.getLatitude(), store.getLongitude()), 16));
            if (store.getId() == storeData.get(i).getStore().getId()) {
                getMarkerInfo(marker);
            }
        }

        mMap.setOnMarkerClickListener(this);

    }
}

