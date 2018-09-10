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
import com.naver.mycnex.viewpageapplication.event.VPSpinnerItemSelected;
import com.naver.mycnex.viewpageapplication.global.Global;
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

    // 뷰페이저 ( Fragment ) 위치
    public static int VP_POS_LEFT = 0;
    public static int VP_POS_RIGHT = 1;

    // OnCreate ( 최초실행 ) 시 중복 BUS 실행 차단 위한 Flag
    private boolean LOCATION_FLAG = false;
    private boolean SIZE_FLAG = false;
    private boolean GENERAL_FLAG = false;
    private boolean SPECIAL_FLAG = true; // 기본 좌측이므로 flag 해제

    // Fragment 로 BUS 전달해 gridView 에서 사용할 Spinner 의 index
    private int LOCATION_IDX = 0;   // [ 전체 ]
    private int SIZE_IDX = 1;       // [ 소형견 ]
    private int GENERAL_IDX = 0;    // [ 전체 ]
    private int SPECIAL_IDX = 0;    // [ 전체 ]

    // 카테고리 Spinner 생성에 사용할 배열 변수
    private static int DEFAULT_ITEM_IDX = 1; // "전체" 는 DB에 없기 때문에 index 에서 빼줘야 한다.
    private String[] CATEGORY_GENERAL_ARR = new String[Global.CATEGORY_GENERAL_LENGTH + DEFAULT_ITEM_IDX];
    private String[] CATEGORY_SPECIAL_ARR = new String[Global.CATEGORY_SPECIAL_LENGTH + DEFAULT_ITEM_IDX];

    // 주소, 반려견 크기 Spinner 생성에 사용할 배열 변수
    private String[] LOCATION_ARR;
    private String[] SIZE_ARR;


    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.spinnerLocate) Spinner spinnerLocate;
    @BindView(R.id.spinnerSize) Spinner spinnerSize;
    @BindView(R.id.spinnerPlaceGeneral) Spinner spinnerPlaceGeneral;
    @BindView(R.id.spinnerPlaceSpecial) Spinner spinnerPlaceSpecial;
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
        dropDownMenuSet();

        // Spinner 아이템 클릭 리스너 등록
        spinnerSetOnItemSelect();

        // ViewPager Adapter 설정
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);

        // ViewPage 화면 변경 리스너
        addOnPageChangeListener();

        DrawerLayout();

        // 뒤로가기 두 번할 경우 앱 종료 기능
        backPressCloseHandler = new BackPressCloseHandler(this);
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
        viewpager.setCurrentItem(VP_POS_LEFT);
    }
    @OnClick (R.id.btnGoRight)   // ViewPager 우측이동
    public void setBtnGoRight(){
        viewpager.setCurrentItem(VP_POS_RIGHT);
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

        // 로그인 여부에 따라 Drawer Layout 내용 설정
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
    public void dropDownMenuSet(){
        // 주소,
        LOCATION_ARR = getResources().getStringArray(R.array.address1);
        ArrayAdapter addressAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, LOCATION_ARR);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocate.setAdapter(addressAdapter);
        // 반려견 크기
        SIZE_ARR = getResources().getStringArray(R.array.dogSize);
        ArrayAdapter dogSizeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, SIZE_ARR);
        dogSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(dogSizeAdapter);

        // 복제 배열 ( 그리고 "전체" value 를 추가한 ) 변수를 사용 - CATEGORY_GENERAL_ARR[], CATEGORY_SPECIAL_ARR[]
        // 애견동반,
        ArrayAdapter placeGeneralAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CATEGORY_GENERAL_ARR);
        placeGeneralAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceGeneral.setAdapter(placeGeneralAdapter);
        //애견전용
        ArrayAdapter placeSpecialAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CATEGORY_SPECIAL_ARR);
        placeSpecialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceSpecial.setAdapter(placeSpecialAdapter);
    }

    // Spinner Arr Setting
    public void dropDownCategoryArrSet(){
        // Spinner ( 드롭다운 메뉴 ) 에 사용될 카테고리의 배열값 할당
        // Static 배열 복사
        System.arraycopy( Global.CATEGORY_GENERAL_STR_ARR, 0, CATEGORY_GENERAL_ARR, 1, Global.CATEGORY_GENERAL_LENGTH );
        System.arraycopy( Global.CATEGORY_SPECIAL_STR_ARR, 0, CATEGORY_SPECIAL_ARR, 1, Global.CATEGORY_SPECIAL_LENGTH );

        // "전체" 항목 추가
        CATEGORY_GENERAL_ARR[0] = "전체";
        CATEGORY_SPECIAL_ARR[0] = "전체";
    }

    // Spinner OnItemClick  ( position 으로 선택 아이템 알 수 있다. )
    public void spinnerSetOnItemSelect() {
        // 지역 선택시
        spinnerLocate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( LOCATION_FLAG ){
                    LOCATION_IDX = position;
                    bus.post(new VPSpinnerItemSelected( LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX, viewpager.getCurrentItem() ));
                } else {
                    LOCATION_FLAG = true;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 반려견 크기 선택시
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( SIZE_FLAG ){
                    SIZE_IDX = Global.PETSIZE_ID_ARR[position];
                    bus.post(new VPSpinnerItemSelected( LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX, viewpager.getCurrentItem() ));
                } else {
                    SIZE_FLAG = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 장소 ( 애견동반 ) 선택시
        spinnerPlaceGeneral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( GENERAL_FLAG ){
                    GENERAL_IDX = position;
                    bus.post(new VPSpinnerItemSelected( LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX, viewpager.getCurrentItem() ));
                } else {
                    GENERAL_FLAG = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 장소 ( 애견전용 ) 선택시
        spinnerPlaceSpecial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( SPECIAL_FLAG ){
                    SPECIAL_IDX = position + Global.CATEGORY_DIVISION_NUM;
                    bus.post(new VPSpinnerItemSelected( LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX, viewpager.getCurrentItem() ));
                } else {
                    SPECIAL_FLAG = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void addOnPageChangeListener(){
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position == VP_POS_LEFT){
                    btnGoLeft.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnGoRight.setBackground(getResources().getDrawable(R.drawable.flat_box_gray));
                    spinnerPlaceGeneral.setVisibility(View.VISIBLE);
                    spinnerPlaceSpecial.setVisibility(View.GONE);
                    bus.post(new VPSpinnerItemSelected( LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX, viewpager.getCurrentItem() ));
                }else if (position == VP_POS_RIGHT){
                    btnGoLeft.setBackground(getResources().getDrawable(R.drawable.flat_box_gray));
                    btnGoRight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    spinnerPlaceSpecial.setVisibility(View.VISIBLE);
                    spinnerPlaceGeneral.setVisibility(View.GONE);
                    bus.post(new VPSpinnerItemSelected( LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX, viewpager.getCurrentItem() ));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void DrawerLayout() {
        // Drawer Layout Adapter 설정
        LoginService loginService = LoginService.getInstance();

        if (loginService.getLoginMember() == null) {
            String[] drawerStrArr = getResources().getStringArray(R.array.DrawerWhenlogout);
            drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerStrArr));
            drawerListView.setOnItemClickListener(new DrawerItemWhenLogIn());
        } else {
            String[] drawerStrArr = getResources().getStringArray(R.array.DrawerWhenlogin);
            drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerStrArr));
            drawerListView.setOnItemClickListener(new DrawerItemWhenLogOut());
        }

    }

    private class DrawerItemWhenLogIn implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0://설정
                    Intent intent = new Intent(ViewPagerActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private class DrawerItemWhenLogOut implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0://즐겨찾기
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

}
