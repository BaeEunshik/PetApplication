package com.naver.mycnex.viewpageapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class JoinActivity extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.id_edit) EditText id_edit;
    @BindView(R.id.nickname_edit) EditText nickname_edit;
    @BindView(R.id.pw_edit) EditText pw_edit;
    @BindView(R.id.pw2_edit) EditText pw2_edit;
    @BindView(R.id.email_edit) EditText email_edit;
    @BindView(R.id.join_finish_btn) Button join_finish_btn;
    @BindView(R.id.btnGoBack)ImageButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.join_finish_btn)
    public void join_finish() {

        String id = id_edit.getText().toString();

        if (id.equals("") || id == null) {
            id_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"아이디를 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.equals("admin")) {
            id_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"이미 사용중인 아이디입니다",Toast.LENGTH_SHORT).show();
            return;
        }
        String nickname = nickname_edit.getText().toString();

        if (nickname.equals("") || nickname == null) {
            nickname_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"닉네임을 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }
        String email = email_edit.getText().toString();

        if (email.equals("") || email == null) {
            email_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"이메일을 입력하세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        String pw = pw_edit.getText().toString();
        String pw2 = pw2_edit.getText().toString();

        if (pw.equals("") || pw == null) {
            pw_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        } else if ((pw2.equals("") || pw2 == null)) {
            pw2_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pw.equals(pw2)) {
            pw_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(JoinActivity.this,nickname+"님 환영합니다",Toast.LENGTH_SHORT).show();
        finish();
    }



    /** onDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){     //뒤로가기

        finish();
    }

}

