package com.naver.mycnex.viewpageapplication;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterShopActivity extends AppCompatActivity {

    public static Integer RESERVATION_ABLE = 0;  // 예약가능 : 0 , 예약불가 : 1
    public static Integer RESERVATION_UNABLE = 1;
    public static Integer RESERVATION = RESERVATION_ABLE;

    //btn_save
    //shop_address
    Unbinder unbinder;
    @BindView(R.id.btn_shop_address) Button btn_shop_address;   //주소버튼
    @BindView(R.id.btnReserveOk) Button btnReserveOk;           //예약버튼
    @BindView(R.id.btnReserveNo) Button btnReserveNo;           //예약버튼
    @BindView(R.id.btnGoBack) ImageButton btnGoBack;            //뒤로가기 버튼

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        //버터나이프
        unbinder = ButterKnife.bind(this);

        //TODO : 영업시간 다이얼로그
        /*
        btn_dialog_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        Log.d("ksj","hour : " + hour + " min : " + minute);
                    }
                };

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(DialogActivity.this,
                        timeSetListener,hour,minute,false);
                dialog.show();
            }
        });
        */
    }

    //onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //버터나이프
        unbinder.unbind();
    }

    /********** OnClick **********/
    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){     //뒤로가기
        finish();
    }
    @OnClick(R.id.btn_shop_address)//주소창 클릭
    public void shop_address(){
        //다음주소검색 API activity 로 이동
        Intent intent = new Intent(RegisterShopActivity.this, GetAddressActivity.class);
        startActivity(intent);
        Log.d("은식","클릭");
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


}
