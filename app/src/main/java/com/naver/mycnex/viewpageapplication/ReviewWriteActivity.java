package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.mycnex.viewpageapplication.adapter.ReviewListAdapter;
import com.naver.mycnex.viewpageapplication.data.Review;
import com.naver.mycnex.viewpageapplication.login.LoginService;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewWriteActivity extends AppCompatActivity {

    private Unbinder unbinder;
    ArrayList<Review>items = new ArrayList<>();
    ReviewListAdapter reviewListAdapter;
    @BindView(R.id.btnGoBack)ImageButton btnGoBack;
    @BindView(R.id.TextPoint)TextView TextPoint=null ;//별점포인트
    @BindView(R.id.ratingBar)RatingBar ratingBar;
    @BindView(R.id.et_input_content)EditText et_input_content;//리뷰쓰기
    @BindView(R.id.btn_add_content)Button btn_add_content;//리뷰추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);
        unbinder = ButterKnife.bind(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // get values and then displayed in a toast
               String totalStars = "Total Stars:: " + ratingBar.getNumStars();

           TextPoint.setText("" +rating);

            }
        });
    }
    @OnClick(R.id.btnGoBack)
    public void btnGoBack(){        // 뒤로가기
        finish();
    }

    @OnClick(R.id.btn_add_content)
    public  void setBtn_input_content(){
        Intent intent = getIntent();
        LoginService loginService = LoginService.getInstance();

        String content = et_input_content.getText().toString();
        String score = TextPoint.getText().toString();
        long store_id = intent.getLongExtra("store_id",-1);
        long member_id = loginService.getLoginMember().getId();

        Call<Void> sendReview = RetrofitService.getInstance().getRetrofitRequest().WriteReview(content, score, store_id,member_id);
        sendReview.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }
}
