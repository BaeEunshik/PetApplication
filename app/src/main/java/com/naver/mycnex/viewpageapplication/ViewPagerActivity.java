package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.naver.mycnex.viewpageapplication.Bus.BusProvider;
import com.naver.mycnex.viewpageapplication.adapter.ViewPagerAdapter;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPagerActivity extends AppCompatActivity {


    //?????
    //????????
    //?????

    ViewPagerAdapter viewPagerAdapter;
    Bus bus = BusProvider.getInstance().getBus();
    Unbinder unbinder;

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.spinner0) Spinner spinner0;
    @BindView(R.id.spinner1) Spinner spinner1;
    @BindView(R.id.spinner2) Spinner spinner2;
    @BindView(R.id.btn_0) Button btn_0;
    @BindView(R.id.btn_1) Button btn_1;
    @BindView(R.id.btn_openDrawer) Button btn_openDrawer;//메뉴버튼
    @BindView(R.id.btnSrchText)ImageButton btnSrchText;//키워드 검색버튼
    @BindView(R.id.btnSrchMap) ImageButton btnSrchMap;//맵 검색버튼

    // 드로어 레이아웃 관련 변수
    private final String[] navItems = {"로그인하러가기", "북마크", "설정", "가게등록" };
    @BindView(R.id.fl_activity_main_container) FrameLayout flContainer;
    @BindView(R.id.dl_activity_main_drawer) DrawerLayout dlDrawer;
    @BindView(R.id.lv_activity_main_nav_list) ListView lvNavList;


    /** onCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        //버터나이프
        unbinder = ButterKnife.bind(this);
        //이벤트버스
        bus.register(this);


        //Spinner ( 드롭다운 메뉴 ) 관련설정
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.address1, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner0.setAdapter(addressAdapter);
        ArrayAdapter dogsizeAdapter = ArrayAdapter.createFromResource(this, R.array.dogSize, android.R.layout.simple_spinner_item);
        dogsizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dogsizeAdapter);
        ArrayAdapter placeAdapter = ArrayAdapter.createFromResource(this, R.array.place, android.R.layout.simple_spinner_item);
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(placeAdapter);

        //ViewPager Adapter 설정
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);

        //Drawer Layout Adapter 설정
        lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        btn_openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
                dlDrawer.openDrawer(lvNavList);
            }

        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0://로그인/로그아웃
                    Intent intent = new Intent(ViewPagerActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                case 1://북마크
                    intent = new Intent(ViewPagerActivity.this, BookmarkList_Activity.class);
                    startActivity(intent);
                    break;
                case 2://설정
                    intent = new Intent(ViewPagerActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case 3://가게등록
                    intent = new Intent(ViewPagerActivity.this, RegisterShopActivity.class);
                    startActivity(intent);
                    break;
            }
            dlDrawer.closeDrawer(lvNavList);
        }

    }

    /********** OnClick **********/
    @OnClick(R.id.btn_openDrawer)    //드로어 레이아웃
    public void btn_openDrawer(){
        Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
        dlDrawer.openDrawer(lvNavList);
    }



    @OnClick (R.id.btn_0)   //ViewPager 좌측이동
    public void btn_0(){
        viewpager.setCurrentItem(0);
    }
    @OnClick (R.id.btn_1)   //ViewPager 우측이동
    public void btn_1(){
        viewpager.setCurrentItem(1);
    }
    @OnClick(R.id.btnSrchText)  // SearchKeywordActivity ( 검색어로 찾기 ) 로 이동
    public void btnSrchText(){
        Intent intent = new Intent(ViewPagerActivity.this, SearchKeywordActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnSrchMap)   // SearchMapActivity ( 맵에서 찾기 ) 로 이동
    public void btnSrchMap(){
        Intent intent = new Intent(ViewPagerActivity.this, SearchMapActivity.class);
        startActivity(intent);
    }

    /** onDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        bus.unregister(this);
    }
}
