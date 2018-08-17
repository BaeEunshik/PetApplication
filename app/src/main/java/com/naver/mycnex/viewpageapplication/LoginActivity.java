package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.login_btn)    //로그인
    public void Login() {
        String id = id_edit.getText().toString();
        String pw = pw_edit.getText().toString();

        if (id.equals("admin") && pw.equals("1234")) {
            Toast.makeText(LoginActivity.this,"계정이 있지만 로그인은 안됩니다.",Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(LoginActivity.this, .class);
//            startActivity(intent);

            // 로그인 성공시 현재 Activity 종료하며 돌아감 ( 로그인멤버 설정되어야 함 )

        } else {
            Toast.makeText(LoginActivity.this,"아이디 혹은 비밀번호가 맞지 않습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.join_btn) //회원가입
    public void Join() {
        Intent intent = new Intent(LoginActivity.this,JoinActivity.class);
        startActivity(intent);
    }

    @BindView(R.id.login_btn) Button login_btn;
    @BindView(R.id.join_btn) Button join_btn;
    @BindView(R.id.id_edit) EditText id_edit;
    @BindView(R.id.pw_edit) EditText pw_edit;
}
