package com.naver.mycnex.viewpageapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreImage;
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// ** 최초실행시 **
// 1. 모든 StoreImage 가져오기 ( 바꿀 예정 )
// 2. 내 위치로 camera 이동
//
// ** Spinner Select 시 **
//  1) 지역, 카테고리( 장소들 ) index 로 데이터 가져오기
//

public class SearchMapActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //구글맵
    private GoogleMap mMap;
    ArrayList<StoreImage> storeImages;
    ArrayList<Store> stores;
    ArrayList<ImageFile> images;
    private ArrayList<Marker> markers;
    boolean check;

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

    // 1. true * 3 되면
    // 2. SQL 보내어 데이터 뿌려줌
    //
    // ....

    // OnCreate ( 최초실행 ) 시 중복 BUS 실행 차단 위한 Flag
    private boolean LOCATION_FLAG = false;
    private boolean CATEGORY_FLAG = false;
    // SQL 보낼 Spinner 의 index
    private int LOCATION_IDX = 0;   // [ 전체 ]
    private int PURPOSE_IDX = 0;
    private int CATEGORY_IDX = 0;    // [ 전체 ]

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
        Permission();
    }
    //onMyLocationClick
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
    //onMyLocationButtonClick
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();

        if (clickCount != null) {

            storeName_txt.setText(stores.get((Integer)marker.getTag()).getName());

            storeLocation_txt.setText(stores.get((Integer)marker.getTag()).getSigungu().toString());
            dog_size_txt.setText(Global.PETSIZE_STR_ARR[stores.get((Integer)marker.getTag()).getDog_size()-1]);

            if (stores.get((Integer)marker.getTag()).getCategory() < Global.CATEGORY_DIVISION_NUM) { // general 일 때
                storeCategory_txt.setText(Global.CATEGORY_GENERAL_STR_ARR[stores.get((Integer)marker.getTag()).getCategory()]);
            } else {
                storeCategory_txt.setText(Global.CATEGORY_SPECIAL_STR_ARR[stores.get((Integer)marker.getTag()).getCategory()-Global.CATEGORY_DIVISION_NUM]);
            }

            check = false;
            for (int i = 0; i < images.size(); i++) {
                if (check) {
                    break;
                }
                if (images.get(i).getStore_id() == stores.get((Integer)marker.getTag()).getId()) {

                    final int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap image = null;
                            try {
                                image = getBitmap(Global.BASE_IMAGE_URL+images.get(finalI).getSavedName());
                                check = true;
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
                }
            }
        }
        return false;
    }

    /********** 권한 관련 **********/
    public void Permission() {
        PermissionListener permissionlistener = new PermissionListener() {
            //권한 획득시
            @Override
            public void onPermissionGranted() {
                getMarkFromServer();
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
        // map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // init Spinner
        dropDownCategoryArrSet();
        dropDownMenuDefaultSet();
        spinnerSetOnItemSelect();
    }

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try{
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url 로 input 받는 flag 허용
            connection.connect(); //연결
            is = connection.getInputStream(); // get inputStream
            retBitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
        }
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
                    LOCATION_IDX = position;
                    Log.d("SearchMapAct_배은식","지역선택 - SQL 보내기");
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
                // 애견동반, 애견전용 Spinner 는 SQL 요청과 무관하다. - 그 뒤에 붙은 spinnerPlace 의 index 를 사용하면 된다.
                switchPlaceSpinner(position);   // 애견동반 or 애견전용 선택에 따른 spinnerPlace 의 아이템 변경 함수
                PURPOSE_IDX = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(CATEGORY_FLAG){
                    if(PURPOSE_IDX == Global.CATEGORY_GENERAL){
                        CATEGORY_IDX = position;                                // default : 0
                    } else if (PURPOSE_IDX == Global.CATEGORY_SPECIAL){
                        CATEGORY_IDX = position + Global.CATEGORY_DIVISION_NUM; // default : 100
                    }
                    Log.d("SearchMapAct_배은식","장소선택 - SQL 보내기");
                    Log.d("value",Integer.toString(LOCATION_IDX)+","+Integer.toString(CATEGORY_IDX));
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
        if (index == 0){ // 애견동반
            ArrayAdapter generalAdapter = new ArrayAdapter(SearchMapActivity.this, android.R.layout.simple_spinner_item, CATEGORY_GENERAL_ARR);
            generalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlace.setAdapter(generalAdapter);
        } else if (index == 1){ //애견전용
            ArrayAdapter generalAdapter = new ArrayAdapter(SearchMapActivity.this, android.R.layout.simple_spinner_item, CATEGORY_SPECIAL_ARR);
            generalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPlace.setAdapter(generalAdapter);
        }
    }

    public void getCurrentLocationAndCircle() {
        if (ContextCompat.checkSelfPermission(SearchMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            return;
        }
        mMap.setOnMyLocationButtonClickListener(SearchMapActivity.this);
        mMap.setOnMyLocationClickListener(SearchMapActivity.this);

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        double lat = 0;
        double lng = 0;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            Toast.makeText(this,"location == null",Toast.LENGTH_SHORT).show();
        }

        LatLng currentLocation = new LatLng(lat, lng);

        CircleOptions circle1KM = new CircleOptions().center(currentLocation) //원점
                .radius(1000)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#95FADB9B")); //배경색

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,16));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        markers = new ArrayList<>();

        for (int i = 0; i < stores.size(); i++) {

            LatLng CurrentLocation = new LatLng(stores.get(i).getLatitude(), stores.get(i).getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(CurrentLocation).title(stores.get(i).getName()));
            marker.setTag(i);
            markers.add(marker);

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        mMap.addCircle(circle1KM);

        mMap.setOnMarkerClickListener(this);
    }

    public void getMarkFromServer() {
       Call<ArrayList<StoreImage>> getstore = RetrofitService.getInstance().getRetrofitRequest().getStoreForMap();
        getstore.enqueue(new Callback<ArrayList<StoreImage>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreImage>> call, Response<ArrayList<StoreImage>> response) {
                if (response.isSuccessful()) {
                    storeImages = response.body();

                    getStoreData();
                    getCurrentLocationAndCircle();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<StoreImage>> call, Throwable t) {

            }
        });
    }

    public void getStoreData() {
        stores = new ArrayList<>();
        images = new ArrayList<>();
        for (int i = 0; i < storeImages.size(); i++) {
            stores.add(storeImages.get(i).getStore());
            for (int j = 0; j < storeImages.get(i).getImage().size(); j++) {
                images.add(storeImages.get(i).getImage().get(j));
            }
        }

    }

}

