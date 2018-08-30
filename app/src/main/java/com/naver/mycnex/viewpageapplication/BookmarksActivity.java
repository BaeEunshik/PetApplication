package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;

import com.naver.mycnex.viewpageapplication.adapter.BookmarkActGridAdapter;
import com.naver.mycnex.viewpageapplication.data.Store;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookmarksActivity extends AppCompatActivity {

    private Unbinder unbinder;
    @BindView(R.id.btnGoBack)ImageButton btnGoBack;
    @BindView(R.id.gridView)GridView gridView;

    ArrayList<Store> bookmarks = new ArrayList<>();
    BookmarkActGridAdapter bookmarkActGridAdapter;

    /** onCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        unbinder = ButterKnife.bind(this);

        //그리드뷰
        bookmarkActGridAdapter = new BookmarkActGridAdapter(bookmarks);
        gridView.setAdapter(bookmarkActGridAdapter);
    }
    /** onDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }
    /********** OnClick **********/
    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){    //뒤로가기

        finish();
    }

}

