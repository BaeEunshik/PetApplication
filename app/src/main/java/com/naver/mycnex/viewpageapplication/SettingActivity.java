package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.login.LoginService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.btnGoBack)ImageButton btnGoBack;
    @BindView(R.id.txtLoginLogout)TextView txtLoginLogout;
    @BindView(R.id.btnLoginLogout)RelativeLayout btnLoginLogout;

    /** OnCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //버터나이프
        unbinder = ButterKnife.bind(this);
    }
    /** onResume **/
    @Override
    protected void onResume() {
        super.onResume();
        // 로그인 / 로그아웃 텍스트
        //    현재 로그아웃 상태 : " 로그인 " 출력
        //    현재 로그인 상태 : " 로그아웃 " 출력
        LoginService loginService = LoginService.getInstance();
        if(loginService.getLoginMember() == null ){
            txtLoginLogout.setText("로그인");
        } else {
            txtLoginLogout.setText("로그아웃");
        }
    }

    /** OnDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /********** OnClick **********/
    @OnClick(R.id.btnGoBack)
    public void btnGoBack(){        // 뒤로가기
        finish();
    }
    @OnClick(R.id.btnLoginLogout)
    public void btnLoginLogout(){   // 로그인 or 로그아웃 클릭 시 실행
        logOut();
    }

    /********** Method **********/
    // 로그아웃
    public void logOut() {
        LoginService loginService = LoginService.getInstance();
        if( loginService.getLoginMember()== null ){
            Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
            startActivity(intent);
        } else {
            loginService.logOut();
            // "로그인" 출력 위해 현재 액티비티 재시작
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

}
