package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.mycnex.viewpageapplication.adapter.CommentListAdapter;
import com.naver.mycnex.viewpageapplication.data.Comment;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReviewItemActivity extends AppCompatActivity {
    private Unbinder unbinder;
    ArrayList<Comment> items = new ArrayList<>();
    CommentListAdapter commentListAdapter;
    int Comcount = 0;


    @BindView(R.id.btnGoBack)ImageButton btnGoBack;
    @BindView(R.id.txt_nickname) TextView txt_nickname;
    @BindView(R.id.txt_review_date) TextView txt_review_date;
    @BindView(R.id.TextPoint) TextView TextPoint;
    @BindView(R.id.txt_content)TextView txt_content;//리뷰내용
    @BindView(R.id.txt_ComCount)TextView txt_ComCount = null;//댓글수
    @BindView(R.id.ibtn_dialog1)ImageButton ibtn_dialog1; //삭제다이얼로그
    @BindView(R.id.lv_comment_list)ListView lv_comment_list;//댓글리스트
    @BindView(R.id.et_input_comment)EditText et_input_comment;//댓글입력
    @BindView(R.id.btn_input_comment)Button btn_input_comment = null;//댓글저장

    Integer year;
    Integer month;
    Integer day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_item);
        unbinder = ButterKnife.bind(this);

        commentListAdapter = new CommentListAdapter(items);
        lv_comment_list.setAdapter(commentListAdapter);

        Intent intent = getIntent();
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);

        setDate(year,month,day);




    }

    public void setDate(Integer year,Integer month,Integer day){
        txt_review_date.setText(year+"-"+month+"-"+day);
    }
    /********** OnClick **********/
    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){     //뒤로가기
        finish();
    }
    @OnClick(R.id.ibtn_dialog1)
    public void setIbtn_dialog1(){
        Toast toast = Toast.makeText(getApplicationContext(), "글을삭제하시겠습니까?", Toast.LENGTH_SHORT);
        finish();}

    @OnClick(R.id.btn_input_comment)
    public  void setBtn_input_comment(){
        Intent intent = new Intent(ReviewItemActivity.this,ReviewWriteActivity.class);
        intent.putExtra("year",year);
        intent.putExtra("month",month+1);
        intent.putExtra("day",day);
        startActivityForResult(intent,0);


        String comment = et_input_comment.getText().toString();
        Comcount++;
        txt_ComCount.setText(""+Comcount);
    }


    }
