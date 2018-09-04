package com.naver.mycnex.viewpageapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.location.Geocoder;
import android.net.Uri;
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
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.util.RealPathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.io.File;
import java.io.IOException;
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
    private String PHONE_FRONT_NUM = "";
    private Integer RESERVATION = Global.RESERVATION_ABLE;
    private Integer PETSIZE_PERMISSION = Global.PETSIZE_SMALL;
    private Integer PARKING = Global.PARKING_UNABLE;

    Integer selectedCategory = 0;
    double lat = 0;
    double lng = 0;
    Uri[] imgUri = null;

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


    // 카테고리 Spinner 생성에 사용할 배열 변수
    private String[] CATEGORY_GENERAL_ARR = new String[Global.CATEGORY_GENERAL_LENGTH];
    private String[] CATEGORY_SPECIAL_ARR = new String[Global.CATEGORY_SPECIAL_LENGTH];


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
                getLatLng();
                Log.d("asd", String.valueOf(lat) + " " + String.valueOf(lng));
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

        Integer sigungu = 0;

        for (int i = 0; i < getResources().getStringArray(R.array.address1).length; i++) {
            if (str[2].equals(getResources().getStringArray(R.array.address1)[i])) {
                sigungu = i;
            }
        }

        String store_info = shop_detail_edit.getText().toString();

        String startTime = startTime_btn.getText().toString();
        String endTime = endTime_btn.getText().toString();
        String operation_time = startTime + "~" + endTime;

        String phone = PHONE_FRONT_NUM + "-" + middlePhone_edit.getText().toString() + "-" + lastPhone_edit.getText().toString();

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

        MultipartBody.Part M_name = MultipartBody.Part.createFormData("name",name);
        MultipartBody.Part M_phone = MultipartBody.Part.createFormData("contact",phone);
        MultipartBody.Part M_petsize = MultipartBody.Part.createFormData("dog_size",String.valueOf(PETSIZE_PERMISSION));
        MultipartBody.Part M_store_info = MultipartBody.Part.createFormData("store_information",store_info);
        MultipartBody.Part M_oper_date = MultipartBody.Part.createFormData("operation_day",oper_date);
        MultipartBody.Part M_operation_time = MultipartBody.Part.createFormData("operation_time",operation_time);
        MultipartBody.Part M_parking = MultipartBody.Part.createFormData("parking",String.valueOf(PARKING));
        MultipartBody.Part M_reservation = MultipartBody.Part.createFormData("reservation",String.valueOf(RESERVATION));
        MultipartBody.Part M_address = MultipartBody.Part.createFormData("address",address);
        MultipartBody.Part M_sigungu = MultipartBody.Part.createFormData("sigungu",String.valueOf(sigungu));
        MultipartBody.Part M_lat = MultipartBody.Part.createFormData("lat",String.valueOf(lat));
        MultipartBody.Part M_lng = MultipartBody.Part.createFormData("lng",String.valueOf(lng));
        MultipartBody.Part M_selectedCategory = MultipartBody.Part.createFormData("category",String.valueOf(selectedCategory));

        final ArrayList<MultipartBody.Part> filePart = new ArrayList<>();
        if (imgUri != null) {
            for (int i = 0; i < imgUri.length; i++) {
                File file = new File(RealPathUtil.getRealPath(RegisterShopActivity.this,imgUri[i]));

                filePart.add(MultipartBody.Part.createFormData("file"+i,
                        file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file)));
            }
        }

         Call<Long> submitStore = RetrofitService.getInstance().getRetrofitRequest().submitStore(filePart,M_name,M_phone,M_petsize,M_store_info,M_oper_date,M_operation_time,
                 M_parking,M_reservation,M_address,M_sigungu,M_lat,M_lng,M_selectedCategory);
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
        dropDownCategoryArrSet();
        spinnerPhoneInit();
        InitOperation_Day();
        setselect_general_special();
        DateOnclickListener();
        spinnerPhoneGetData();
        getLastPhoneNumberFocus();
        Parking();
        setOnClickImage();

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
                        imgUri = new Uri[uriList.size()];
                        if (uriList != null) {
                            InitImage();
                            select_photo_btn.setVisibility(View.GONE);
                            for (int i = 0; i < uriList.size(); i++) {
                                Uri uriOne = uriList.get(i);
                                imgUri[i] = uriList.get(i);
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
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item); // purpose array 의 어댑터
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //스피너 아이템 적용
        select_general_special.setAdapter(addressAdapter);  //setAdapter

        select_general_special.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {   // OnItemSelect 리스너
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == Global.CATEGORY_GENERAL) {  // "애견동반" 을 선택한 경우


                    ArrayAdapter placeAdapter = new ArrayAdapter(RegisterShopActivity.this, android.R.layout.simple_spinner_item, CATEGORY_GENERAL_ARR);
                    placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    select_category.setAdapter(placeAdapter);

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

                    ArrayAdapter placeAdapter = new ArrayAdapter(RegisterShopActivity.this, android.R.layout.simple_spinner_item, CATEGORY_SPECIAL_ARR);
                    placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    select_category.setAdapter(placeAdapter);

                    select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < Global.CATEGORY_SPECIAL_LENGTH; i++) {
                                if (position == i) {
                                    selectedCategory = position + Global.CATEGORY_SPECIAL_CAFE + Global.CATEGORY_DIVISION_NUM;
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

    // Spinner Arr Setting
    public void dropDownCategoryArrSet(){
        // Spinner ( 드롭다운 메뉴 ) 에 사용될 카테고리의 배열값 할당
        // Static 배열 복사
        System.arraycopy( Global.CATEGORY_GENERAL_STR_ARR, 0, CATEGORY_GENERAL_ARR, 0, Global.CATEGORY_GENERAL_LENGTH );
        System.arraycopy( Global.CATEGORY_SPECIAL_STR_ARR, 0, CATEGORY_SPECIAL_ARR, 0, Global.CATEGORY_SPECIAL_LENGTH );
    }

    public void spinnerPhoneGetData() {
        spinnerPhone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PHONE_FRONT_NUM = String.valueOf(parent.getItemAtPosition(position));
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

    public void setOnClickImage() {
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
