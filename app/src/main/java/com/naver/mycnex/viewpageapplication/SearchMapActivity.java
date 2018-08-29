package com.naver.mycnex.viewpageapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SearchMapActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    //구글맵
    private GoogleMap mMap;
    //버터나이프
    Unbinder unbinder;
    @BindView(R.id.btnSrchText) ImageButton btnSrchText;
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;
    @BindView(R.id.spinnerLocate) Spinner spinnerLocate;
    @BindView(R.id.spinnerPurpose) Spinner spinnerPurpose;
    @BindView(R.id.spinnerPlace) Spinner spinnerPlace;

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

    /********** 권한 관련 **********/
    public void Permission() {
        PermissionListener permissionlistener = new PermissionListener() {
            //권한 획득시
            @Override
            public void onPermissionGranted() {
                getCurrentLocationAndCircle();
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
        //맵 화면 컴포넌트
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        InitSpinner();
    }

    public void InitSpinner() {
        //Spinner ( 드롭다운 메뉴 ) 관련설정
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.address1, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocate.setAdapter(addressAdapter);
        ArrayAdapter dogsizeAdapter = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item);
        dogsizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPurpose.setAdapter(dogsizeAdapter);
        ArrayAdapter placeAdapter = ArrayAdapter.createFromResource(this, R.array.place, android.R.layout.simple_spinner_item);
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlace.setAdapter(placeAdapter);
    }

    public void getCurrentLocationAndCircle() {
        if (ContextCompat.checkSelfPermission(SearchMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        mMap.setOnMyLocationButtonClickListener(SearchMapActivity.this);
        mMap.setOnMyLocationClickListener(SearchMapActivity.this);

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        double lat =  location.getLatitude();
        double lng = location.getLongitude();
        LatLng currentLocation = new LatLng(lat, lng);

        CircleOptions circle1KM = new CircleOptions().center(currentLocation) //원점
                .radius(1000)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        mMap.addCircle(circle1KM);
    }

    public void setOnClickCurrentLocation() {



    }

}

