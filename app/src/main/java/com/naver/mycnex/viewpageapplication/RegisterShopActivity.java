package com.naver.mycnex.viewpageapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.mycnex.viewpageapplication.Address.Address;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.Builder;

public class RegisterShopActivity extends AppCompatActivity {

    private final int ADDRESS_SUBMIT = 11;
    private final int CAMERA_GALLERY = 111;

    public static Integer RESERVATION_ABLE = 0;  // 예약가능 : 0 , 예약불가 : 1
    public static Integer RESERVATION_UNABLE = 1;
    public static Integer RESERVATION = RESERVATION_ABLE;
    public static Integer PETSIZE_PERMISSION = 1;
    public static Integer PETSIZE_SMALL = 1;
    public static Integer PETSIZE_MIDDLE = 2;
    public static Integer PETSIZE_LARGE = 3;
    public static Integer PARKING_ABLE = 1;
    public static Integer PARKING_UNABLE = 2;
    public static Integer PARKING_VALET = 3;
    public static Integer PARKING = PARKING_UNABLE;
    public static String OPERATION_DATE = "";
    public static String PHONE_NUMBER = "";

    public static Integer GENERAL_CAFE = 11;
    public static Integer GENERAL_RESTAURANT = 12;
    public static Integer GENERAL_BAR = 13;
    public static Integer GENERAL_ACCOMMODATION = 14;
    public static Integer GENERAL_PARK = 15;
    public static Integer GENERAL_STUDIO = 16;

    public static Integer SPECIAL_CAFE = 111;
    public static Integer SPECIAL_PETSHOP = 112;
    public static Integer SPECIAL_BEAUTY = 113;
    public static Integer SPECIAL_HOSPITAL = 114;
    public static Integer SPECIAL_ADOPT = 115;
    public static Integer SPECIAL_EDUCATION = 116;
    public static Integer SPECIAL_ACCOMMODATION = 117;
    public static Integer SPECIAL_LEISURE = 118;
    public static Integer SPECIAL_FUNERAL = 119;
    public static Integer SPECIAL_PLAYGROUND = 120;
    public static Integer SPECIAL_STUDIO = 121;


    double lat = 0;
    double lng = 0;

    boolean[] bool_date;
    String[] str_date = {"월","화","수","목","금","토","일"};
    ImageView[] images = new ImageView[6];
    Button[] buttons = new Button[7];

    Unbinder unbinder;
    @BindView(R.id.middlePhone_edit) EditText middlePhone_edit;
    @BindView(R.id.lastPhone_edit) EditText lastPhone_edit;
    @BindView(R.id.shop_name) TextView shop_name;
    @BindView(R.id.btn_shop_address) Button btn_shop_address;   //주소버튼
    @BindView(R.id.btnReserveOk) Button btnReserveOk;           //예약버튼
    @BindView(R.id.btnReserveNo) Button btnReserveNo;           //예약버튼
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;            //뒤로가기 버튼
    @BindView(R.id.smallsize_btn) Button smallsize_btn;
    @BindView(R.id.middlesize_btn) Button middlesize_btn;
    @BindView(R.id.largesize_btn) Button largesize_btn;
    @BindView(R.id.spinnerPhone) Spinner spinnerPhone;
    @BindView(R.id.parking_able_btn) Button parking_able_btn;
    @BindView(R.id.parking_unable_btn) Button parking_unable_btn;
    @BindView(R.id.parking_valet_btn) Button parking_valet_btn;
    @BindView(R.id.startTime_btn) Button startTime_btn;
    @BindView(R.id.endTime_btn) Button endTime_btn;
    @BindView(R.id.btnMonday) Button btnMonday;
    @BindView(R.id.btnTuesday) Button btnTuesday;
    @BindView(R.id.btnThursday) Button btnThursday;
    @BindView(R.id.btnWednesday) Button btnWednesday;
    @BindView(R.id.btnFriday) Button btnFriday;
    @BindView(R.id.btnSaturday) Button btnSaturday;
    @BindView(R.id.btnSunday) Button btnSunday;
    @BindView(R.id.submit_btn) Button submit_btn;
    @BindView(R.id.select_photo_btn) Button select_photo_btn;

    @BindView(R.id.image1) ImageView image1;
    @BindView(R.id.image2) ImageView image2;
    @BindView(R.id.image3) ImageView image3;
    @BindView(R.id.image4) ImageView image4;
    @BindView(R.id.image5) ImageView image5;
    @BindView(R.id.image6) ImageView image6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        //버터나이프
        unbinder = ButterKnife.bind(this);
        InitWhenCreated();

    }

    //onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //버터나이프
        unbinder.unbind();
    }

    @Override // Activity Result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_SUBMIT) {
            if (resultCode == RESULT_OK) {
                String address = data.getStringExtra("address");
                btn_shop_address.setText(address);
            }
        } else if (requestCode == CAMERA_GALLERY) {
            if (resultCode == RESULT_OK) {

                ClipData clipData = data.getClipData();

                if (clipData != null) {

                    InitImage();
                    select_photo_btn.setVisibility(View.GONE);
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri urione =  clipData.getItemAt(i).getUri();
                        images[i].setImageURI(urione);
                        images[i].setVisibility(View.VISIBLE);
                    }

                }
            }
        }


    }

    /********** OnClick **********/
    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){     //뒤로가기
        finish();
    }
    @OnClick(R.id.btn_shop_address)//주소창 클릭
    public void shop_address(){
        //다음주소검색 API activity 로 이동
        Intent intent = new Intent(RegisterShopActivity.this, Address.class);
        startActivityForResult(intent,ADDRESS_SUBMIT);
    }

    @OnClick(R.id.btnReserveOk)    // 개인 / 업체 가입자 버튼 클릭 이벤트
    public void btnIndividual(){
        RESERVATION = RESERVATION_ABLE;
        switchMemberType();
    }
    @OnClick(R.id.btnReserveNo)
    public void btnCompany(){
        RESERVATION = RESERVATION_UNABLE;
        switchMemberType();
    }

    @OnClick(R.id.smallsize_btn)
    public void smallsize() {
        PETSIZE_PERMISSION = 1;
        PetSizeSelection();
    }
    @OnClick(R.id.middlesize_btn)
    public void middlesize() {
        PETSIZE_PERMISSION = 2;
        PetSizeSelection();
    }
    @OnClick(R.id.largesize_btn)
    public void largesize() {
        PETSIZE_PERMISSION = 3;
        PetSizeSelection();
    }

    @OnClick(R.id.parking_able_btn)
    public void parkingAble() {
        PARKING = PARKING_ABLE;
        Parking();
    }
    @OnClick(R.id.parking_unable_btn)
    public void parkingUnable() {
        PARKING = PARKING_UNABLE;
        Parking();
    }
    @OnClick(R.id.parking_valet_btn)
    public void parkingValet() {
        PARKING = PARKING_VALET;
        Parking();
    }

    // 운영시간
    @OnClick(R.id.startTime_btn)
    public void startTime() {

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {

                    String hour = "";

                    if (hourOfDay <= 9) {
                        hour = "0" + String.valueOf(hourOfDay);
                    } else {
                        hour = String.valueOf(hourOfDay);
                    }

                    String str_minute = "";

                    if (minute <= 9) {
                        str_minute = "0" + String.valueOf(minute);
                    } else {
                        str_minute = String.valueOf(minute);
                    }

                    startTime_btn.setText(hour+":"+str_minute);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterShopActivity.this, AlertDialog.THEME_HOLO_LIGHT, myTimeListener, 0, 0, true);
        timePickerDialog.setTitle("시작시간을 선택하세요");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    @OnClick(R.id.endTime_btn)
    public void endTime() {
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {

                    String hour = "";

                    if (hourOfDay <= 9) {
                        hour = "0" + String.valueOf(hourOfDay);
                    } else {
                        hour = String.valueOf(hourOfDay);
                    }

                    String str_minute = "";

                    if (minute <= 9) {
                        str_minute = "0" + String.valueOf(minute);
                    } else {
                        str_minute = String.valueOf(minute);
                    }

                    endTime_btn.setText(hour+":"+str_minute);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterShopActivity.this, AlertDialog.THEME_HOLO_LIGHT, myTimeListener, 0, 0, true);
        timePickerDialog.setTitle("종료시간을 선택하세요");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    @OnClick(R.id.select_photo_btn)
    public void select_photo_btn() {
        Permission();
    }

    /*************** 등록 버튼 ******************/
    @OnClick(R.id.submit_btn)
    public void submit() {

        String name = shop_name.getText().toString();
        String address = btn_shop_address.getText().toString();
        getLatLng();

        for (int i = 0; i < bool_date.length; i++) {
            if (bool_date[i] == false) {
                OPERATION_DATE += str_date[i];
            }
        }

        String startTime = startTime_btn.getText().toString();
        String endTime = endTime_btn.getText().toString();
        PHONE_NUMBER += "-" + middlePhone_edit.getText().toString() + "-" + lastPhone_edit.getText().toString();

        if (name == null || name.equals("")) {
            Toast.makeText(RegisterShopActivity.this, "가게명을 입력하세요", Toast.LENGTH_SHORT).show();
            shop_name.requestFocus();
            shop_name.setFocusableInTouchMode(true);
            return;
        }

        if (address == null || address.equals("")) {
            Toast.makeText(RegisterShopActivity.this, "주소를 입력하세요", Toast.LENGTH_SHORT).show();
            btn_shop_address.requestFocus();
            btn_shop_address.setFocusableInTouchMode(true);
            return;
        }

        if (OPERATION_DATE == "") {
            Toast.makeText(RegisterShopActivity.this, "영업시간을 선택하세요", Toast.LENGTH_SHORT).show();
            btnMonday.requestFocus();
            btnMonday.setFocusableInTouchMode(true);
            return;
        }

        if (PHONE_NUMBER.length() < 9) {
            Toast.makeText(RegisterShopActivity.this, "번호를 입력하세요", Toast.LENGTH_SHORT).show();
            middlePhone_edit.requestFocus();
            middlePhone_edit.setFocusableInTouchMode(true);
            return;
        }

        Log.d("asd",name);
        Log.d("asd", address);
        if (PETSIZE_PERMISSION == PETSIZE_SMALL) {
            Log.d("asd", "PETSIZE_SMALL");
        } else if (PETSIZE_PERMISSION == PETSIZE_MIDDLE) {
            Log.d("asd", "PETSIZE_MIDDLE");
        } else if (PETSIZE_PERMISSION == PETSIZE_LARGE) {
            Log.d("asd", "PETSIZE_LARGE");
        }
        Log.d("asd",OPERATION_DATE);
        Log.d("asd", startTime + "~" + endTime);
        if (RESERVATION == RESERVATION_ABLE) {
            Log.d("asd","RESERVATION_ABLE");
        } else {
            Log.d("asd","RESERVATION_UNABLE");
        }
        Log.d("asd",PHONE_NUMBER);

        if (PARKING == PARKING_ABLE) {
            Log.d("asd","PARKING_ABLE");
        } else if (PARKING == PARKING_UNABLE) {
            Log.d("asd","PARKING_UNABLE");
        } else if (PARKING == PARKING_VALET) {
            Log.d("asd","PARKING_VALET");
        }

        Log.d("asd", String.valueOf(lat) + " " + String.valueOf(lng));

//        Intent intent = new Intent(RegisterShopActivity.this, ShopActivity.class);
//        startActivity(intent);
    }
    /********** METHOD **********/

    // 예약에 대한 함수
    // 예약가능 / 불가 버튼 클릭시 view 와 함께
    // static 변수 RESERVATION 값을 변경 - DB 에 저장하는 값으로 사용
    public void switchMemberType(){
        if(RESERVATION == RESERVATION_ABLE) {
            btnReserveOk.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnReserveOk.setTextColor(Color.parseColor("#FFFFFF"));
            btnReserveNo.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            btnReserveNo.setTextColor(Color.parseColor("#000000"));
        } else if (RESERVATION == RESERVATION_UNABLE) {
            btnReserveOk.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnReserveOk.setTextColor(Color.parseColor("#000000"));
            btnReserveNo.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_green));
            btnReserveNo.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void PetSizeSelection() {

        if (PETSIZE_PERMISSION == PETSIZE_SMALL) {

            middlesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            middlesize_btn.setTextColor(Color.parseColor("#000000"));

            largesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            largesize_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PETSIZE_PERMISSION == PETSIZE_MIDDLE) {

            middlesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            middlesize_btn.setTextColor(Color.parseColor("#FFFFFF"));

            largesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            largesize_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PETSIZE_PERMISSION == PETSIZE_LARGE) {

            middlesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            middlesize_btn.setTextColor(Color.parseColor("#FFFFFF"));

            largesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            largesize_btn.setTextColor(Color.parseColor("#FFFFFF"));

        }
    }

    // Oncreated시 초기화(initiation)
    public void InitWhenCreated() {
        // 핸드폰 번호 카테고리
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.phoneNumSelect, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(addressAdapter);

        bool_date = new boolean[7];

        for (int i = 0; i < bool_date.length; i++) {
            bool_date[i] = true;
        }

        DateOnclickListener();

        spinnerPhone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PHONE_NUMBER = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getLastPhoneNumberFocus();
        Parking();

        images[0] = image1;
        images[1] = image2;
        images[2] = image3;
        images[3] = image4;
        images[4] = image5;
        images[5] = image6;

        InitImage();

        for (int i = 0; i < images.length; i++) {
            images[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentToCameraGAllery();
                }
            });
        }

    }

    public void getLastPhoneNumberFocus() {
        middlePhone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (middlePhone_edit.length() == 4) {
                    lastPhone_edit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void InitImage() {
        for (int i = 0; i < 6; i++) {
            images[i].setImageResource(0);
            images[i].setVisibility(View.GONE);
        }
    }

    public void IntentToCameraGAllery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),  CAMERA_GALLERY);
    }

    public void Parking() {

        if (PARKING == PARKING_ABLE) {

            parking_able_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            parking_able_btn.setTextColor(Color.parseColor("#FFFFFF"));

            parking_unable_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_unable_btn.setTextColor(Color.parseColor("#000000"));

            parking_valet_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_valet_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PARKING == PARKING_UNABLE) {

            parking_able_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_able_btn.setTextColor(Color.parseColor("#000000"));

            parking_unable_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            parking_unable_btn.setTextColor(Color.parseColor("#FFFFFF"));

            parking_valet_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_valet_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PARKING == PARKING_VALET) {

            parking_able_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_able_btn.setTextColor(Color.parseColor("#000000"));

            parking_unable_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_unable_btn.setTextColor(Color.parseColor("#000000"));

            parking_valet_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            parking_valet_btn.setTextColor(Color.parseColor("#FFFFFF"));

        }
    }

    public void Permission() {
        PermissionListener permissionlistener = new PermissionListener() {
            //권한 획득시
            @Override
            public void onPermissionGranted() {

                IntentToCameraGAllery();

            }
            //권한 거부시
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(RegisterShopActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("갤러리 접근 권한을 거부하셨습니다. \n[설정] > [권한]에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    public void getLatLng() {
        Geocoder geocoder = new Geocoder(RegisterShopActivity.this);

        try {

            List<android.location.Address> addr = geocoder.getFromLocationName( btn_shop_address.getText().toString(),1); // 주소가 저장된 값을 넣으면 된다. Address 는 안드로이드가 제공하는 객체.
            lat = addr.get(0).getLatitude();
            lng = addr.get(0).getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DateOnclickListener() {
        buttons[0] = btnMonday;
        buttons[1] = btnTuesday;
        buttons[2] = btnWednesday;
        buttons[3] = btnThursday;
        buttons[4] = btnFriday;
        buttons[5] = btnSaturday;
        buttons[6] = btnSunday;

        for (int i = 0; i < buttons.length; i++) {
            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bool_date[finalI]) {

                        buttons[finalI].setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
                        buttons[finalI].setTextColor(Color.parseColor("#FFFFFF"));

                        bool_date[finalI] = false;
                    } else {

                        buttons[finalI].setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
                        buttons[finalI].setTextColor(Color.parseColor("#000000"));

                        bool_date[finalI] = true;
                    }
                }
            });
        }
    }
}
