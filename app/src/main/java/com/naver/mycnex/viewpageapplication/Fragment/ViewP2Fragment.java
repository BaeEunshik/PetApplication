package com.naver.mycnex.viewpageapplication.Fragment;

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
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ViewP2Fragment extends Fragment {
   /* private static ViewP2Fragment curr = null;
    public static ViewP2Fragment getInstance() {
        if (curr == null) {
            curr = new ViewP2Fragment();
        }
        return curr;
    }*/

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
        // 이벤트 버스
        bus.register(this);
        // 버터나이프
        unbinder = ButterKnife.bind(this,view);


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
}
