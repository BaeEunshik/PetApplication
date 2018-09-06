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

import com.naver.mycnex.viewpageapplication.ShopActivity;
import com.naver.mycnex.viewpageapplication.ViewPagerActivity;
import com.naver.mycnex.viewpageapplication.adapter.VP2GridAdapter;
import com.naver.mycnex.viewpageapplication.bus.BusProvider;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.ImageFile;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.naver.mycnex.viewpageapplication.data.StoreImage;
import com.naver.mycnex.viewpageapplication.event.VPSpinnerItemSelected;
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.retrofit.RetrofitService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@NoArgsConstructor
public class VP2Fragment extends Fragment {

    ArrayList<Store> stores;
    ArrayList<ImageFile> images;
    ArrayList<StoreImage> storeimages;
    VP2GridAdapter vp2GridAdapter;

    // 이벤트 버스
    Bus bus = BusProvider.getInstance().getBus();
    // 버터나이프
    private Unbinder unbinder;
    @BindView(R.id.gridView) GridView gridView;


    /** onCreateView **/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vp2, container, false);
        bus.register(this);
        unbinder = ButterKnife.bind(this,view);
        InitWhenCreated();
        return view;
    }
    /** onDestroy **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 이벤트 버스
        bus.unregister(this);
        // 버터나이프
        unbinder.unbind();
    }

    public void InitWhenCreated() {
        getDataFromServerWithId(0,Global.PETSIZE_SMALL,0);
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
    public void getDataFromServerWithId(Integer sigungu, Integer dog_size, Integer category ) {
        Call<ArrayList<StoreImage>> getStoreData = RetrofitService.getInstance().getRetrofitRequest().getStoreSpecial( sigungu, dog_size, category );
        getStoreData.enqueue(new Callback<ArrayList<StoreImage>>() {
            @Override
            public void onResponse(Call<ArrayList<StoreImage>> call, Response<ArrayList<StoreImage>> response) {
                if (response.isSuccessful()) {
                    storeimages = response.body();
                    getData();
                    initAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StoreImage>> call, Throwable t) {

            }
        });
    }
    public void initAdapter() {
        vp2GridAdapter = new VP2GridAdapter(stores,images);
        gridView.setAdapter(vp2GridAdapter);
    }

    public void getData() {

        stores = new ArrayList<>();
        images = new ArrayList<>();

        for (int i = 0; i < storeimages.size(); i++) {
            stores.add(storeimages.get(i).getStore());
            images.add(storeimages.get(i).getImage().get(0));
        }
    }

    /******************** EVENT BUS ********************/
    @Subscribe
    public void vPSpinnerItemSelected(VPSpinnerItemSelected evt){

        // 부모 액티비티에서 현재 화면이 LEFT or RIGHT 에 따라 다른 값을 넣어 프래그먼트마다 구분
        if(evt.getViewPager_state() == ViewPagerActivity.VP_POS_RIGHT){

            int sigungu = evt.getLocation_idx();
            int dog_size = evt.getSize_idx();
            int category = evt.getGeneral_idx();

            Log.d("asd", "VP2 : " + dog_size);

            getDataFromServerWithId( sigungu, dog_size, category );

            Log.d("VP2_BUS_LOCATION",Integer.toString(evt.getLocation_idx()));
            Log.d("VP2_BUS_SIZE",Integer.toString(evt.getSize_idx()));
            Log.d("VP2_BUS_SPECIAL",Integer.toString(evt.getSpecial_idx()));
        }

    }

}
