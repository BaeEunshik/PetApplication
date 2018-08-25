package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.adapter.CommentListAdapter;
import com.naver.mycnex.viewpageapplication.data.Comment;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReviewItemActivity extends AppCompatActivity {
    private Unbinder unbinder;
    ArrayList<Comment> items = new ArrayList<>();
    CommentListAdapter commentListAdapter;

    @BindView(R.id.btnGoBack)ImageButton btnGoBack;
    @BindView(R.id.txt_nickname) TextView txt_nickname;
    @BindView(R.id.txt_point) TextView txt_point;
    @BindView(R.id.txt_content)TextView txt_content;
    @BindView(R.id.txt_ComCount)TextView txt_ComCount;
    @BindView(R.id.ibtn_review) ImageButton ibtn_review;
    @BindView(R.id.ibtn_dialog1)ImageButton ibtn_dialog1;
    @BindView(R.id.lv_comment_list)ListView lv_comment_list;
    @BindView(R.id.btn_comment_save)Button btn_comment_save;
    @BindView(R.id.et_input_comment)EditText et_input_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_item);
        unbinder = ButterKnife.bind(this);




    }


}
