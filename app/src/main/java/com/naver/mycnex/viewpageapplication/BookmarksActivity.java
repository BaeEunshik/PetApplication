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

        // 임시 객체리스트 생성
        bookmarks.add(
                new Store(1,"NAME_1",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강남구","논현동",10.00,20.00,1
                ));
        bookmarks.add(
                new Store(2,"NAME_2",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강동구","둔촌동",10.00,20.00,1
                ));
        bookmarks.add(
                new Store(3,"NAME_3",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강서구","무슨동",10.00,20.00,1
                ));
        bookmarks.add(
                new Store(4,"NAME_4",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강북구","어떤동",10.00,20.00,1
                ));
        bookmarks.add(
                new Store(5,"NAME_5",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 송파구","이런동",10.00,20.00,1
                ));

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

