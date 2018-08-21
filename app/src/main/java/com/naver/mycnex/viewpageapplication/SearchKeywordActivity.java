package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchKeywordActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.btnGoBack)ImageButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_keyword);

        unbinder = ButterKnife.bind(this);



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

