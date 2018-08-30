package com.naver.mycnex.viewpageapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.naver.mycnex.viewpageapplication.bus.BusProvider;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.ShopActivity;
import com.naver.mycnex.viewpageapplication.adapter.VP1GridAdapter;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@NoArgsConstructor
public class ViewP1Fragment extends Fragment {
    //싱글톤
    /*private static ViewP1Fragment curr = null;
    public static ViewP1Fragment getInstance() {
        if (curr == null) {
            curr = new ViewP1Fragment();
        }
        return curr;
    }*/

    //그리드뷰
    ArrayList<Store> stores;
    VP1GridAdapter vp1GridAdapter;

    //버터나이프
    private Unbinder unbinder;
    @BindView(R.id.gridView) GridView gridView;

    //이벤트버스
    Bus bus = BusProvider.getInstance().getBus();

    /** onCreateView **/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vp1, container, false);
        bus.register(this);
        unbinder = ButterKnife.bind(this,view);
        InitWhenCreated();
        return view;
    }

    /** onResume **/
    @Override
    public void onResume() {
        super.onResume();
        InitWhenCreated();
    }
    /** onDestroy **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        unbinder.unbind();
    }

    public void InitWhenCreated() {
        getDataFromServer();
        GridViewOnItemClick();
    }

    public void GridViewOnItemClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ShopActivity.class);
                intent.putExtra("id",stores.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void getDataFromServer() {
        Call<ArrayList<Store>> getStoreData = RetrofitService.getInstance().getRetrofitRequest().getStoreGeneral();
        getStoreData.enqueue(new Callback<ArrayList<Store>>() {
            @Override
            public void onResponse(Call<ArrayList<Store>> call, Response<ArrayList<Store>> response) {
                if (response.isSuccessful()) {
                    stores = response.body();
                    initAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Store>> call, Throwable t) {

            }
        });
    }

    public void initAdapter() {
        vp1GridAdapter = new VP1GridAdapter(stores);
        gridView.setAdapter(vp1GridAdapter);
    }

}
