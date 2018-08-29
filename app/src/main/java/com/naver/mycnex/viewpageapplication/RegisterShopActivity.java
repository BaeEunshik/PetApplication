package com.naver.mycnex.viewpageapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Parcelable;
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

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.mycnex.viewpageapplication.address.Address;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitRequest;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import gun0912.tedbottompicker.TedBottomPicker;
import retrofit2.Call;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterShopActivity extends AppCompatActivity {

    private final int ADDRESS_SUBMIT = 11;
    //전역변수
    private String PHONE_NUMBER = "";
    private Integer RESERVATION = Global.RESERVATION_ABLE;
    private Integer PETSIZE_PERMISSION = Global.PETSIZE_SMALL;
    private Integer PARKING = Global.PARKING_UNABLE;

    Integer selectedCategory = 0;
    double lat = 0;
    double lng = 0;

    boolean[] bool_date;
    String[] str_date = {"월","화","수","목","금","토","일"};
    ImageView[] images = new ImageView[6];
    Button[] buttons = new Button[7];

    Unbinder unbinder;
    @BindView(R.id.shop_detail_edit) EditText shop_detail_edit;
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
    @BindView(R.id.select_general_special) Spinner select_general_special;
    @BindView(R.id.select_category) Spinner select_category;

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
        RESERVATION = Global.RESERVATION_ABLE;
        switchReservationType();
    }
    @OnClick(R.id.btnReserveNo)
    public void btnCompany(){
        RESERVATION = Global.RESERVATION_UNABLE;
        switchReservationType();
    }

    @OnClick(R.id.smallsize_btn)
    public void smallsize() {
        PETSIZE_PERMISSION = Global.PETSIZE_SMALL;
        PetSizeSelection();
    }
    @OnClick(R.id.middlesize_btn)
    public void middlesize() {
        PETSIZE_PERMISSION = Global.PETSIZE_MIDIUM;
        PetSizeSelection();
    }
    @OnClick(R.id.largesize_btn)
    public void largesize() {
        PETSIZE_PERMISSION = Global.PETSIZE_LARGE;
        PetSizeSelection();
    }

    @OnClick(R.id.parking_able_btn)
    public void parkingAble() {
        PARKING = Global.PARKING_ABLE;
        Parking();
    }
    @OnClick(R.id.parking_unable_btn)
    public void parkingUnable() {
        PARKING = Global.PARKING_UNABLE;
        Parking();
    }
    @OnClick(R.id.parking_valet_btn)
    public void parkingValet() {
        PARKING = Global.PARKING_VALET;
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
        PermissionImg();
    }

    /*************** 등록 버튼 ******************/
    @OnClick(R.id.submit_btn)
    public void submit() {

        String name = shop_name.getText().toString();
        String address = btn_shop_address.getText().toString();
        String[] str = address.split(" ");

        String sigungu = str[2];
        String dong = str[3];

        getLatLng();

        String store_info = shop_detail_edit.getText().toString();

        String startTime = startTime_btn.getText().toString();
        String endTime = endTime_btn.getText().toString();
        String operation_time = startTime + "~" + endTime;

        String phone = PHONE_NUMBER + "-" + middlePhone_edit.getText().toString() + "-" + lastPhone_edit.getText().toString();

        String oper_date = "";
        for (int i = 0; i < bool_date.length; i++) {
            if (bool_date[i] == false) {
                oper_date += str_date[i];
            }
        }

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

        /* 상세설명을 원하지 않을 수도 있기 때문에, 일단은 주석처리.
        if (store_info == null || store_info.equals("")) {
            Toast.makeText(RegisterShopActivity.this, "상세설명을 입력하세요", Toast.LENGTH_SHORT).show();
            shop_detail_edit.requestFocus();
            shop_detail_edit.setFocusableInTouchMode(true);
            return;
        }
        */

        if (oper_date == "") {
            Toast.makeText(RegisterShopActivity.this, "영업시간을 선택하세요", Toast.LENGTH_SHORT).show();
            btnMonday.requestFocus();
            btnMonday.setFocusableInTouchMode(true);
            return;
        }

        if (phone.length() < 9) {
            Toast.makeText(RegisterShopActivity.this, "번호를 입력하세요", Toast.LENGTH_SHORT).show();
            middlePhone_edit.requestFocus();
            middlePhone_edit.setFocusableInTouchMode(true);
            return;
        }

         Call<Long> submitStore = RetrofitService.getInstance().getRetrofitRequest().submitStore(name,phone,PETSIZE_PERMISSION,store_info,oper_date,operation_time,
                 PARKING,RESERVATION,address,sigungu,dong,lat,lng,selectedCategory);
         submitStore.enqueue(new Callback<Long>() {
             @Override
             public void onResponse(Call<Long> call, Response<Long> response) {
                 if (response.isSuccessful()) {
                     Long id = response.body();

                     Toast.makeText(RegisterShopActivity.this,"매장이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(RegisterShopActivity.this, ShopActivity.class);
                     intent.putExtra("id", id);
                     startActivity(intent);
                 }
             }

             @Override
             public void onFailure(Call<Long> call, Throwable t) {

             }
         });
    }
    /********** METHOD **********/

    // Oncreated시 초기화(initiation)
    public void InitWhenCreated() {

        spinnerPhoneInit();
        InitOperation_Day();
        setselect_general_special();
        DateOnclickListener();
        spinnerPhoneGetData();
        getLastPhoneNumberFocus();
        Parking();
        setOnClickimage();

    }

    // 예약에 대한 함수
    // 예약가능 / 불가 버튼 클릭시 view 와 함께
    // static 변수 RESERVATION 값을 변경 - DB 에 저장하는 값으로 사용
    public void switchReservationType(){
        if(RESERVATION == Global.RESERVATION_ABLE) {
            btnReserveOk.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnReserveOk.setTextColor(Color.parseColor("#FFFFFF"));
            btnReserveNo.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            btnReserveNo.setTextColor(Color.parseColor("#000000"));
        } else if (RESERVATION == Global.RESERVATION_UNABLE) {
            btnReserveOk.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnReserveOk.setTextColor(Color.parseColor("#000000"));
            btnReserveNo.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_green));
            btnReserveNo.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void PetSizeSelection() {

        if (PETSIZE_PERMISSION == Global.PETSIZE_SMALL) {

            middlesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            middlesize_btn.setTextColor(Color.parseColor("#000000"));

            largesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            largesize_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PETSIZE_PERMISSION == Global.PETSIZE_MIDIUM) {

            middlesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            middlesize_btn.setTextColor(Color.parseColor("#FFFFFF"));

            largesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            largesize_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PETSIZE_PERMISSION == Global.PETSIZE_LARGE) {

            middlesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            middlesize_btn.setTextColor(Color.parseColor("#FFFFFF"));

            largesize_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            largesize_btn.setTextColor(Color.parseColor("#FFFFFF"));

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

    public void Parking() {

        if (PARKING == Global.PARKING_ABLE) {

            parking_able_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            parking_able_btn.setTextColor(Color.parseColor("#FFFFFF"));

            parking_unable_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_unable_btn.setTextColor(Color.parseColor("#000000"));

            parking_valet_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_valet_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PARKING == Global.PARKING_UNABLE) {

            parking_able_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_able_btn.setTextColor(Color.parseColor("#000000"));

            parking_unable_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            parking_unable_btn.setTextColor(Color.parseColor("#FFFFFF"));

            parking_valet_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_valet_btn.setTextColor(Color.parseColor("#000000"));

        } else if (PARKING == Global.PARKING_VALET) {

            parking_able_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_able_btn.setTextColor(Color.parseColor("#000000"));

            parking_unable_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_right_white));
            parking_unable_btn.setTextColor(Color.parseColor("#000000"));

            parking_valet_btn.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            parking_valet_btn.setTextColor(Color.parseColor("#FFFFFF"));

        }
    }

    public void PermissionImg() {
        PermissionListener permissionlistener = new PermissionListener() {
            //권한 획득시
            @Override
            public void onPermissionGranted() {
                getImageFromTedPicker();
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
    public void getImageFromTedPicker(){
        // 이미지 피커 실행
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(RegisterShopActivity.this)
                .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(ArrayList<Uri> uriList) {
                        // uriList 활용
                        if (uriList != null) {
                            InitImage();
                            select_photo_btn.setVisibility(View.GONE);
                            for (int i = 0; i < uriList.size(); i++) {
                                Uri uriOne = uriList.get(i);
                                images[i].setImageURI(uriOne);
                                images[i].setVisibility(View.VISIBLE);
                            }

                        }
                    }
                })
                .setSelectMaxCountErrorText(" 6장까지 선택 가능합니다. ")
                .setSelectMaxCount(6)
                .setCompleteButtonText("확인")
                .setEmptySelectionText("No Select")
                .showCameraTile(false)
                .showGalleryTile(false)
                .showTitle(false)
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());
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

    public void setselect_general_special() {

        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_general_special.setAdapter(addressAdapter);

        select_general_special.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == Global.CATEGORY_GENERAL) {

                    ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(RegisterShopActivity.this, R.array.petGeneral, android.R.layout.simple_spinner_item);
                    addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    select_category.setAdapter(addressAdapter);

                    select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < Global.CATEGORY_GENERAL_LENGTH; i++) {
                                if (position == i) {
                                    selectedCategory = position + Global.CATEGORY_GENERAL_CAFE;
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else if (position == Global.CATEGORY_SPECIAL) {

                    ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(RegisterShopActivity.this, R.array.petSpecial, android.R.layout.simple_spinner_item);
                    addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    select_category.setAdapter(addressAdapter);

                    select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < Global.CATEGORY_SPECIAL_LENGTH; i++) {
                                if (position == i) {
                                    selectedCategory = position + Global.CATEGORY_SPECIAL_CAFE;
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void spinnerPhoneGetData() {
        spinnerPhone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PHONE_NUMBER = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void spinnerPhoneInit() {
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.phoneNumSelect, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(addressAdapter);
    }

    public void InitOperation_Day() {
        bool_date = new boolean[7];

        for (int i = 0; i < bool_date.length; i++) {
            bool_date[i] = true;
        }
    }

    public void setOnClickimage() {
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
                    getImageFromTedPicker();
                }
            });
        }
    }
}
