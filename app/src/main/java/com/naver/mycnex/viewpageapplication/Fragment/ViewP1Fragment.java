package com.naver.mycnex.viewpageapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.naver.mycnex.viewpageapplication.Bus.BusProvider;
import com.naver.mycnex.viewpageapplication.R;
import com.naver.mycnex.viewpageapplication.ShopActivity;
import com.naver.mycnex.viewpageapplication.ShopListActivity;
import com.naver.mycnex.viewpageapplication.adapter.VP1GridAdapter;
import com.naver.mycnex.viewpageapplication.data.Store;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;


public class ViewP1Fragment extends Fragment {
    //싱글톤
    private static ViewP1Fragment curr = null;
    public static ViewP1Fragment getInstance() {
        if (curr == null) {
            curr = new ViewP1Fragment();
        }
        return curr;
    }

    //그리드뷰
    ArrayList<Store> storeList = new ArrayList<>();
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

        // 임시 객체리스트 생성
        storeList.add(
                new Store(1,"NAME_1",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강남구","논현동",10.00,20.00,1
                ));
        storeList.add(
                new Store(2,"NAME_2",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강동구","둔촌동",10.00,20.00,1
                ));
        storeList.add(
                new Store(3,"NAME_3",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 강서구","무슨동",10.00,20.00,1
                ));
        storeList.add(
                new Store(4,"NAME_4",000,2,"information","00:00","24:00",
                2,1,"address","서울시 강북구","어떤동",10.00,20.00,1
        ));
        storeList.add(
                new Store(5,"NAME_5",000,2,"information","00:00","24:00",
                        2,1,"address","서울시 송파구","이런동",10.00,20.00,1
                ));

        // 그리드뷰
        vp1GridAdapter = new VP1GridAdapter(storeList);
        gridView.setAdapter(vp1GridAdapter);

        return view;
    }

    /********** OnItemClick **********/
    @OnItemClick(R.id.gridView)
    public void gridView(int position){
        // TODO :
        // 실행되는 ShopActivity 는 클릭한 요소의 정보를 담고 있어야 한다.
        Intent intent = new Intent(getActivity(),ShopActivity.class);
        startActivity(intent);
    }

    /** onResume **/
    @Override
    public void onResume() {
        super.onResume();
    }
    /** onDestroy **/
    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        unbinder.unbind();
    }
}
