package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import retrofit2.Call;

import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.id_edit) EditText id_edit;
    @BindView(R.id.nickname_edit) EditText nickname_edit;
    @BindView(R.id.pw_edit) EditText pw_edit;
    @BindView(R.id.pw2_edit) EditText pw2_edit;
    @BindView(R.id.join_finish_btn) Button join_finish_btn;
    @BindView(R.id.btnGoBack)ImageButton btnGoBack;

    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        unbinder = ButterKnife.bind(this);

    }

    @OnClick(R.id.join_finish_btn)
    public void join_finish() {
        OnclickJoinButton();
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

    public void OnclickJoinButton() {
        String id = id_edit.getText().toString();

        if (id.equals("") || id == null) {
            id_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"아이디를 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }

        String nickname = nickname_edit.getText().toString();
        name = nickname;

        if (nickname.equals("") || nickname == null) {
            nickname_edit.requestFocus();
            Toast.makeText(JoinActivity.this,"닉네임을 입력하세요",Toast.LENGTH_SHORT).show();
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

        Call<Boolean> joinMember = RetrofitService.getInstance().getRetrofitRequest().joinMember(id,pw,nickname);
        joinMember.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean checkId = response.body();

                    if (checkId == false) {
                        Toast.makeText(JoinActivity.this, "가입 중인 아이디입니다.",Toast.LENGTH_SHORT).show();
                        id_edit.requestFocus();
                    } else {
                        Toast.makeText(JoinActivity.this, name+"님 환영합니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

}

