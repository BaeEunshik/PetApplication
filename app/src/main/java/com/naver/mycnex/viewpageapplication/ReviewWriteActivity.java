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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.Data;
import lombok.ToString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Data
@ToString
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

        Calendar cal = Calendar.getInstance();

        Integer year = cal.get(Calendar.YEAR);
        Integer month = (cal.get(Calendar.MONTH)+1);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        String date = year+"-"+month+"-"+day;

        String content = et_input_content.getText().toString();
        String score = TextPoint.getText().toString();

        final long store_id = intent.getLongExtra("store_id",-1);
        long member_id = loginService.getLoginMember().getId();

        if (content == null || content.equals("")) {
            Toast.makeText(this,"댓글을 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> sendReview = RetrofitService.getInstance().getRetrofitRequest().WriteReview(content, score, store_id,member_id,date);
        sendReview.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Intent intent = new Intent();
                intent.putExtra("store_id",store_id);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }
}
