package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        // 로그인 / 로그아웃 텍스트
        //    현재 로그아웃 상태 : " 로그인 " 출력
        //    현재 로그인 상태 : " 로그아웃 " 출력

        /*
            if( 로그아웃된 상태 ){
               txtLoginLogout.setText("로그인");
            } else{
               txtLoginLogout.setText("로그아웃");
            }
        * */

    }
    /** OnDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /********** OnClick **********/
    @OnClick(R.id.btnLoginLogout)
    public void btnLoginLogout(){
        // 로그인 여부에 따라 loginActivity 이동 || 로그아웃 ( loginMember 삭제 )
        /*
        if(로그인){
            loginActivity 이동
        } else {
            로그아웃 ( loginMember 삭제 ),
            loginActivity 이동
        }
        */
    }
}
