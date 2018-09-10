package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;


import com.naver.mycnex.viewpageapplication.adapter.SearchGridAdapter;
import com.naver.mycnex.viewpageapplication.data.ImageFile;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreData;
import com.naver.mycnex.viewpageapplication.data.StoreImage;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchKeywordActivity extends AppCompatActivity {

    Unbinder unbinder;
    ArrayList<StoreData> storeDatas;
    ArrayList<StoreData> tmpStoreDatas;
    SearchGridAdapter searchGridAdapter;

    @BindView(R.id.btnGoBack)ImageButton btnGoBack;
    @BindView(R.id.searchStore_gv) GridView searchStore_gv;
    @BindView(R.id.searchStore_edit) EditText searchStore_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_keyword);
        unbinder = ButterKnife.bind(this);
        initWhenCreated();


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

    public void initWhenCreated() {
        getStoreData();
        SearchStore();
        OnClickGridView();
    }

    public void getStoreData() {

        Call<ArrayList<StoreData>> getStoreData = RetrofitService.getInstance().getRetrofitRequest().GetStoreForSearch();
        getStoreData.enqueue(new Callback<ArrayList<StoreData>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreData>> call, Response<ArrayList<StoreData>> response) {
                if (response.isSuccessful()) {
                    storeDatas = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StoreData>> call, Throwable t) {

            }
        });

    }

    public void SearchStore() {

        searchStore_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {

                    tmpStoreDatas = new ArrayList<>();

                    for (int i = 0; i < storeDatas.size(); i++) {
                        if (storeDatas.get(i).getStore().getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            tmpStoreDatas.add(storeDatas.get(i));
                        }
                    }

                    initGridViewAdapter(tmpStoreDatas);

                } else {

                    tmpStoreDatas = new ArrayList<>();
                    initGridViewAdapter(tmpStoreDatas);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initGridViewAdapter(ArrayList<StoreData> tmpStoreDatas) {

        searchGridAdapter = new SearchGridAdapter(tmpStoreDatas);
        searchStore_gv.setAdapter(searchGridAdapter);

    }

    public void OnClickGridView() {

        searchStore_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchKeywordActivity.this, ShopActivity.class);
                intent.putExtra("id", tmpStoreDatas.get(position).getStore().getId());
                startActivity(intent);
            }
        });

    }

}

