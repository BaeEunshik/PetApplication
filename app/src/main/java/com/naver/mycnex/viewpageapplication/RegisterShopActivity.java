package com.naver.mycnex.viewpageapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.naver.mycnex.viewpageapplication.Address.Address;

import java.time.LocalDateTime;
import java.util.Calendar;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.Builder;

public class RegisterShopActivity extends AppCompatActivity {

    public static Integer ADDRESS_SUBMIT_OK = 2;
    public static Integer RESERVATION_ABLE = 0;  // 예약가능 : 0 , 예약불가 : 1
    public static Integer RESERVATION_UNABLE = 1;
    public static Integer RESERVATION = RESERVATION_ABLE;
    public static Integer PETSIZE_PERMISSION = 1;
    public static Integer PETSIZE_SMALL = 1;
    public static Integer PETSIZE_MIDDLE = 2;
    public static Integer PETSIZE_LARGE = 3;
    public static Integer PARKING = 0;
    public static Integer PARKING_ABLE = 1;
    public static Integer PARKING_UNABLE = 2;
    public static Integer PARKING_VALET = 3;
    public static String OPERATION_DATE = "";
    public static Integer MONDAY = 0;
    public static Integer TUESDAY = 1;
    public static Integer WEDNESDAY = 2;
    public static Integer THURSDAY = 3;
    public static Integer FRIDAY = 4;
    public static Integer SATURDAY = 5;
    public static Integer SUNDAY = 6;
    boolean[] bool_date;
    String[] str_date = {"월","화","수","목","금","토","일"};

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

    @Override // 주소 정보 받아오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_SUBMIT_OK) {
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
        startActivityForResult(intent,ADDRESS_SUBMIT_OK);
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

    // 운영요일
    @OnClick(R.id.btnMonday)
    public void btnMonday() {

        if (bool_date[MONDAY]) {

            btnMonday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnMonday.setTextColor(Color.parseColor("#FFFFFF"));

            bool_date[MONDAY] = false;
        } else {

            btnMonday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnMonday.setTextColor(Color.parseColor("#000000"));

            bool_date[MONDAY] = true;
        }
    }
    @OnClick(R.id.btnTuesday)
    public void btnTuesday() {
        if (bool_date[TUESDAY]) {
            btnTuesday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnTuesday.setTextColor(Color.parseColor("#FFFFFF"));

            bool_date[TUESDAY] = false;
        } else {
            btnTuesday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnTuesday.setTextColor(Color.parseColor("#000000"));

            bool_date[TUESDAY] = true;
        }
    }
    @OnClick(R.id.btnThursday)
    public void btnThursday() {
        if (bool_date[THURSDAY]) {
            btnThursday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnThursday.setTextColor(Color.parseColor("#FFFFFF"));

            bool_date[THURSDAY] = false;
        } else {
            btnThursday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnThursday.setTextColor(Color.parseColor("#000000"));

            bool_date[THURSDAY] = true;
        }
    }
    @OnClick(R.id.btnWednesday)
    public void btnWednesday() {
        if (bool_date[WEDNESDAY]) {
            btnWednesday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnWednesday.setTextColor(Color.parseColor("#FFFFFF"));

            bool_date[WEDNESDAY] = false;
        } else {
            btnWednesday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnWednesday.setTextColor(Color.parseColor("#000000"));

            bool_date[WEDNESDAY] = true;
        }
    }
    @OnClick(R.id.btnFriday)
    public void btnFriday() {
        if (bool_date[FRIDAY]) {
            btnFriday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnFriday.setTextColor(Color.parseColor("#FFFFFF"));

            bool_date[FRIDAY] = false;
        } else {
            btnFriday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnFriday.setTextColor(Color.parseColor("#000000"));

            bool_date[FRIDAY] = true;
        }
    }
    @OnClick(R.id.btnSunday)
    public void btnSunday() {
        if (bool_date[SUNDAY]) {
            btnSunday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnSunday.setTextColor(Color.parseColor("#FFFFFF"));

            bool_date[SUNDAY] = false;
        } else {
            btnSunday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnSunday.setTextColor(Color.parseColor("#000000"));

            bool_date[SUNDAY] = true;
        }
    }
    @OnClick(R.id.btnSaturday)
    public void btnSaturday() {
        if (bool_date[SATURDAY]) {
            btnSaturday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_green));
            btnSaturday.setTextColor(Color.parseColor("#FFFFFF"));

            bool_date[SATURDAY] = false;
        } else {
            btnSaturday.setBackground(ContextCompat.getDrawable(RegisterShopActivity.this,R.drawable.button_left_white));
            btnSaturday.setTextColor(Color.parseColor("#000000"));

            bool_date[SATURDAY] = true;
        }
    }

    @OnClick(R.id.submit_btn)   //등록
    public void submit() {

        String name = shop_name.getText().toString();
        String address = btn_shop_address.getText().toString();

        for (int i = 0; i < bool_date.length; i++) {
            if (bool_date[i] == false) {
                OPERATION_DATE += str_date[i];
            }
        }

        String startTime = startTime_btn.getText().toString();
        String endTime = endTime_btn.getText().toString();
        String phone = middlePhone_edit.getText().toString() + lastPhone_edit.getText().toString();

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
        Log.d("asd",phone);

        if (PARKING == PARKING_ABLE) {
            Log.d("asd","PARKING_ABLE");
        } else if (PARKING == PARKING_UNABLE) {
            Log.d("asd","PARKING_UNABLE");
        } else if (PARKING == PARKING_VALET) {
            Log.d("asd","PARKING_VALET");
        }

        // Intent 사용하여 새 액티비티 띄우면 : ViewPagerActivity 2장 띄워지는 상황 ㅜㅜ
        /*Intent intent = new Intent(RegisterShopActivity.this, ViewPagerActivity.class);
        startActivity(intent);*/
        // 액티비티 종료로 바탕에 이미 띄워진 뷰페이저 사용
        finish();
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

    public void InitWhenCreated() {
        // 핸드폰 번호 카테고리
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.phoneNumSelect, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(addressAdapter);

        bool_date = new boolean[7];

        for (int i = 0; i < bool_date.length; i++) {
            bool_date[i] = true;
        }
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

}
