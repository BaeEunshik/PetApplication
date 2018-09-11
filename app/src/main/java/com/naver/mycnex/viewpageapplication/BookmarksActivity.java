package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.naver.mycnex.viewpageapplication.adapter.BookmarkActGridAdapter;
import com.naver.mycnex.viewpageapplication.data.ImageFile;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreData;
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

public class BookmarksActivity extends AppCompatActivity {

    ArrayList<StoreData> storeData;
    BookmarkActGridAdapter bookmarkActGridAdapter;

    private Unbinder unbinder;

    @BindView(R.id.btnGoBack)ImageButton btnGoBack;
    @BindView(R.id.gridView)GridView gridView;

    /** onCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        unbinder = ButterKnife.bind(this);
        initWhenCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBookMarkData();
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

    public void initWhenCreated() {
        GridviewSetOnClick();
    }

    public void getBookMarkData() {
        LoginService loginService = LoginService.getInstance();

        Call<ArrayList<StoreData>> getBookMarkData = RetrofitService.getInstance().getRetrofitRequest().getBookmarkStore(loginService.getLoginMember().getId());
        getBookMarkData.enqueue(new Callback<ArrayList<StoreData>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreData>> call, Response<ArrayList<StoreData>> response) {
                if (response.isSuccessful()) {
                    storeData = response.body();
                    initBookmarkAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StoreData>> call, Throwable t) {

            }
        });
    }

    public void initBookmarkAdapter() {
        bookmarkActGridAdapter = new BookmarkActGridAdapter(storeData);
        gridView.setAdapter(bookmarkActGridAdapter);
    }

    public void GridviewSetOnClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookmarksActivity.this,ShopActivity.class);
                intent.putExtra("id",storeData.get(position).getStore().getId());
                startActivity(intent);
            }
        });
    }

}

