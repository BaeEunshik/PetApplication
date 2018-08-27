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
import android.widget.Spinner;
import android.widget.TextView;

import com.naver.mycnex.viewpageapplication.BackPress.BackPressCloseHandler;
import com.naver.mycnex.viewpageapplication.bus.BusProvider;
import com.naver.mycnex.viewpageapplication.adapter.ViewPagerAdapter;
import com.naver.mycnex.viewpageapplication.login.LoginService;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPagerActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;

    ViewPagerAdapter viewPagerAdapter;
    Bus bus = BusProvider.getInstance().getBus();
    Unbinder unbinder;

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.spinnerLocate) Spinner spinnerLocate;
    @BindView(R.id.spinnerSize) Spinner spinnerSize;
    @BindView(R.id.spinnerPlace) Spinner spinnerPlace;
    @BindView(R.id.btnGoLeft) Button btnGoLeft;
    @BindView(R.id.btnGoRight) Button btnGoRight;
    @BindView(R.id.btn_openDrawer) Button btn_openDrawer;//메뉴버튼
    @BindView(R.id.btnSrchText)ImageButton btnSrchText;//키워드 검색버튼
    @BindView(R.id.btnSrchMap) ImageButton btnSrchMap;//맵 검색버튼

    // 드로어 레이아웃 관련 변수
    private final String[] navItems = {"북마크", "설정", "내가게","가게등록"};
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

        Log.d("은식","뷰페이저 onCreate");

        //버터나이프
        unbinder = ButterKnife.bind(this);
        //이벤트버스
        bus.register(this);

        //Spinner ( 드롭다운 메뉴 ) 관련설정
        DropDownSetting();

        //ViewPager Adapter 설정
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);

        //Drawer Layout Adapter 설정
        drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        // 뒤로가기 두 번할 경우 앱 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        // OnPageChange
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {

                if ( position == 0 ){
                    Log.d("은식","0번째");
                } else if( position == 1 ){
                    Log.d("은식","1번째");
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
        viewpager.setCurrentItem(0);
    }
    @OnClick (R.id.btnGoRight)   // ViewPager 우측이동
    public void setBtnGoRight(){
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
    public void DropDownSetting() {
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.address1, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocate.setAdapter(addressAdapter);
        ArrayAdapter dogsizeAdapter = ArrayAdapter.createFromResource(this, R.array.dogSize, android.R.layout.simple_spinner_item);
        dogsizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(dogsizeAdapter);
        ArrayAdapter placeAdapter = ArrayAdapter.createFromResource(this, R.array.place, android.R.layout.simple_spinner_item);
        placeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlace.setAdapter(placeAdapter);
    }

}
