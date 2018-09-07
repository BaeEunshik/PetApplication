package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.naver.mycnex.viewpageapplication.data.Member;
import com.naver.mycnex.viewpageapplication.login.LoginService;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //

    Unbinder unbinder;
    @BindView(R.id.login_btn) Button login_btn;
    @BindView(R.id.join_btn) Button join_btn;
    @BindView(R.id.id_edit) EditText id_edit;
    @BindView(R.id.pw_edit) EditText pw_edit;
    @BindView(R.id.btnGoBack)ImageButton btnGoBack;

    String id = "";
    String pw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.login_btn)    //로그인
    public void Login() {
        onClickLoginBtn();
    }

    @OnClick(R.id.join_btn) //회원가입
    public void Join() {
        Intent intent = new Intent(LoginActivity.this,JoinActivity.class);
        startActivity(intent);
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

    public void onClickLoginBtn() {

       id = id_edit.getText().toString();
       pw = pw_edit.getText().toString();

        final Call<Boolean> loginService = RetrofitService.getInstance().getRetrofitRequest().login(id,pw);
        loginService.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean check = response.body();

                    if (check) {
                        LoginService loginService = LoginService.getInstance();
                        Member member = new Member(id,pw);

                        loginService.setLoginMember(member);

                        Intent intent = getIntent();
                        if (intent.getIntExtra("review",-1) == 1) {
                            setResult(RESULT_OK,intent);
                            finish();
                        } else {
                            Intent intent1 = new Intent(LoginActivity.this, ViewPagerActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent1);
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호가 맞지 않습니다.",Toast.LENGTH_SHORT).show();
                        id_edit.requestFocus();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

}

