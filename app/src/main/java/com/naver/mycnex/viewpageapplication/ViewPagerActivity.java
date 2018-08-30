package com.naver.mycnex.viewpageapplication;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.BackPress.BackPressCloseHandler;
import com.naver.mycnex.viewpageapplication.bus.BusProvider;
import com.naver.mycnex.viewpageapplication.adapter.ViewPagerAdapter;
import com.naver.mycnex.viewpageapplication.global.Global;
import com.naver.mycnex.viewpageapplication.login.LoginService;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPagerActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;

    ViewPagerAdapter viewPagerAdapter;
    Bus bus = BusProvider.getInstance().getBus();
    Unbinder unbinder;

    // 뷰페이저 위치
    private static int STATE_LEFT = 0;
    private static int STATE_RIGHT = 1;

    // 카테고리 Spinner 에 사용할 배열 변수
    private String[] CATEGORY_GENERAL = new String[Global.CATEGORY_GENERAL_STR_ARR.length + 1];
    private String[] CATEGORY_SPECIAL = new String[Global.CATEGORY_SPECIAL_STR_ARR.length + 1];

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.spinnerLocate) Spinner spinnerLocate;
    @BindView(R.id.spinnerSize) Spinner spinnerSize;
    @BindView(R.id.spinnerPlace) Spinner spinnerPlace;
    @BindView(R.id.btnGoLeft) RelativeLayout btnGoLeft;
    @BindView(R.id.btnGoRight) RelativeLayout btnGoRight;
    @BindView(R.id.btn_openDrawer) Button btn_openDrawer;//메뉴버튼
    @BindView(R.id.btnSrchText)ImageButton btnSrchText; //키워드 검색버튼
    @BindView(R.id.btnSrchMap) ImageButton btnSrchMap;  //맵 검색버튼
    // 드로어 레이아웃 관련 변수
    @BindView(R.id.DrawerWrapper) DrawerLayout DrawerWrapper;
    @BindView(R.id.fl_activity_main_container) FrameLayout flContainer;
    @BindView(R.id.drawerListView) ListView drawerListView;
    @BindView(R.id.drawerBox) LinearLayout drawerBox;
    @BindView(R.id.loginContentBox) LinearLayout loginContentBox;
    @BindView(R.id.MemberProfileBox) LinearLayout MemberProfileBox;
    @BindView(R.id.memberName) TextView memberName;
    @BindView(R.id.btnGoLogin) TextView btnGoLogin;

    /** onCreate **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        // 버터나이프
        unbinder = ButterKnife.bind(this);
        // 이벤트버스
        bus.register(this);

        // Spinner ( 드롭다운 메뉴 ) 에 사용될 카테고리의 Arr 설정
        dropDownCategoryArrSet();

        // Spinner ( 드롭다운 메뉴 ) 관련설정
        basicDropDownSet();
        vpPosLeftDropDownSet();

        // ViewPager Adapter 설정
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);

        // Drawer Layout Adapter 설정
        String[] drawerStrArr = getResources().getStringArray(R.array.DrawerNav);
        drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerStrArr));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        // 뒤로가기 두 번할 경우 앱 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        // Spinner 아이템 클릭 리스너
        spinnerSetOnItemClick();

    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0://북마크
                    Intent intent = new Intent(ViewPagerActivity.this, BookmarksActivity.class);
                    startActivity(intent);
                    break;
                case 1://설정
                    intent = new Intent(ViewPagerActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case 2://내 가게
                    intent = new Intent(ViewPagerActivity.this, ShopListActivity.class);
                    startActivity(intent);
                    break;
                case 3://가게등록
                    intent = new Intent(ViewPagerActivity.this, RegisterShopActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    /** onBackPressed **/
    @Override
    public void onBackPressed() {   // 뒤로가기 두 번 - 앱 종료
        backPressCloseHandler.onBackPressed();
    }

    /********** OnClick **********/
    @OnClick(R.id.btn_openDrawer)    //드로어 레이아웃
    public void btn_openDrawer(){
        DrawerWrapper.openDrawer(drawerBox);
    }
    @OnClick(R.id.drawerBox)
    public void drawerBox(){
        // 드로어 레이아웃 떴을 때 - 뒤에 있는 버튼 이벤트 방지
    }
    @OnClick(R.id.btnGoLogin)    // LoginActivity 로 이동
    public void btnGoLogin(){
        Intent intent = new Intent(ViewPagerActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    @OnClick (R.id.btnGoLeft)   // ViewPager 좌측이동
    public void btnGoLeft(){
        btnGoLeft.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnGoRight.setBackground(getResources().getDrawable(R.drawable.flat_box_gray));
        viewpager.setCurrentItem(STATE_LEFT);
        vpPosLeftDropDownSet(); //spinner 변경
    }
    @OnClick (R.id.btnGoRight)   // ViewPager 우측이동
    public void setBtnGoRight(){
        btnGoLeft.setBackground(getResources().getDrawable(R.drawable.flat_box_gray));
        btnGoRight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        viewpager.setCurrentItem(STATE_RIGHT);
        vpPosRightDropDownSet(); //spinner 변경
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
    /** onResume **/
    @Override
    protected void onResume() {
        super.onResume();
        // 로그인 여부에 따라
        // Drawer Layout 내용 설정
        LoginService loginService = LoginService.getInstance();

        if(loginService.getLoginMember()==null ){
            MemberProfileBox.setVisibility(View.GONE);
            loginContentBox.setVisibility(View.VISIBLE);
        } else {
            memberName.setText(loginService.getLoginMember().getName());
            loginContentBox.setVisibility(View.GONE);
            MemberProfileBox.setVisibility(View.VISIBLE);
        }
    }
    /** onDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        bus.unregister(this);
    }

    // Spinner setting
    public void basicDropDownSet(){
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.address1, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocate.setAdapter(addressAdapter);
        ArrayAdapter dogsizeAdapter = ArrayAdapter.createFromResource(this, R.array.dogSize, android.R.layout.simple_spinner_item);
        dogsizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(dogsizeAdapter);
    }
    public void vpPosLeftDropDownSet(){
        ArrayAdapter placeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CATEGORY_GENERAL);
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlace.setAdapter(placeAdapter);
    }
    public void vpPosRightDropDownSet(){
        ArrayAdapter placeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CATEGORY_SPECIAL);
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlace.setAdapter(placeAdapter);
    }

    // Spinner Arr Setting
    public void dropDownCategoryArrSet(){
        //Spinner ( 드롭다운 메뉴 ) 에 사용될 카테고리의 Arr 설정

        int tmpLength = Global.CATEGORY_GENERAL_STR_ARR.length;
        System.arraycopy( Global.CATEGORY_GENERAL_STR_ARR, 0, CATEGORY_GENERAL, 1, tmpLength );
        CATEGORY_GENERAL[0] = "전체";

        tmpLength = Global.CATEGORY_SPECIAL_STR_ARR.length;
        System.arraycopy( Global.CATEGORY_SPECIAL_STR_ARR, 0, CATEGORY_SPECIAL, 1, tmpLength );
        CATEGORY_SPECIAL[0] = "전체";
    }

    // Spinner OnItemClick
    public void spinnerSetOnItemClick() {
        //지역 선택시
        spinnerLocate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //반려견 크기 선택시
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //장소 선택시
        spinnerPlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //position 으로 선택 아이템 알 수 있음
                if(viewpager.getCurrentItem() == STATE_LEFT){

                } else if (viewpager.getCurrentItem() == STATE_RIGHT){

                } else {
                    Log.d("카테고리","에러");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
