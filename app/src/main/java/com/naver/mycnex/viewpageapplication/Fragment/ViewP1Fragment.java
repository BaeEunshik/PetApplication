package com.naver.mycnex.viewpageapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naver.mycnex.viewpageapplication.Bus.BusProvider;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.squareup.otto.Bus;

import java.util.ArrayList;


public class ViewP1Fragment extends Fragment {
    //싱글톤
    private static ViewP1Fragment curr = null;
    public static ViewP1Fragment getInstance() {
        if (curr == null) {
            curr = new ViewP1Fragment();
        }

        return curr;
    }

    ArrayList<Store> storeList = new ArrayList<>();


    //이벤트버스
    Bus bus = BusProvider.getInstance().getBus();
    /** onCreateView **/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view1_layout, container, false);
        bus.register(this);

        return view;
    }

    /** onDestroy **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
