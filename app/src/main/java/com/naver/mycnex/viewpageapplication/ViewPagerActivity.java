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
import com.squareup.otto.Subscribe;

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

    // Fragment 에 전달해 gridView 에서 사용할 Spinner 의 index
    int defaultNum = -1;
    public int LOCATION_IDX = defaultNum;
    public int SIZE_IDX = defaultNum;
    public int GENERAL_IDX = defaultNum;
    public int SPECIAL_IDX = defaultNum;

    // 카테고리 Spinner 생성에 사용할 배열 변수
    private static int CATEGORY_ADD_ITEM_NUM = 1;
    private String[] CATEGORY_GENERAL = new String[Global.CATEGORY_GENERAL_LENGTH + CATEGORY_ADD_ITEM_NUM];
    private String[] CATEGORY_SPECIAL = new String[Global.CATEGORY_SPECIAL_LENGTH + CATEGORY_ADD_ITEM_NUM];

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
        spinnerSetOnItemClick();

        // ViewPager Adapter 설정
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);

        // Drawer Layout Adapter 설정
        String[] drawerStrArr = getResources().getStringArray(R.array.DrawerNav);
        drawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerStrArr));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        // 뒤로가기 두 번할 경우 앱 종료
        backPressCloseHandler = new BackPressCloseHandler(this);



        // GONE 되어있어도 ItemSelected 뜨는지 확인
        // 카테고리 "전체" 에 set 해줘야 함
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
        spinnerPlaceGeneral.setVisibility(View.VISIBLE);
        spinnerPlaceSpecial.setVisibility(View.GONE);
    }
    @OnClick (R.id.btnGoRight)   // ViewPager 우측이동
    public void setBtnGoRight(){
        btnGoLeft.setBackground(getResources().getDrawable(R.drawable.flat_box_gray));
        btnGoRight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        viewpager.setCurrentItem(STATE_RIGHT);
        spinnerPlaceSpecial.setVisibility(View.VISIBLE);
        spinnerPlaceGeneral.setVisibility(View.GONE);
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
    public void dropDownMenuSet(){
        // 주소, 반려견 크기
        // array.xml 에 지정해놓은 메뉴 사용
        ArrayAdapter addressAdapter = ArrayAdapter.createFromResource(this, R.array.address1, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocate.setAdapter(addressAdapter);
        ArrayAdapter dogSizeAdapter = ArrayAdapter.createFromResource(this, R.array.dogSize, android.R.layout.simple_spinner_item);
        dogSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(dogSizeAdapter);

        // 장소 카테고리 ( 애견동반, 애견전용 )
        // Global 의 static 배열을 복제 ( 그리고 "전체" 항목 추가 ) 해 사용한다. - CATEGORY_GENERAL[], CATEGORY_SPECIAL[]
        ArrayAdapter placeGeneralAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CATEGORY_GENERAL);
        placeGeneralAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceGeneral.setAdapter(placeGeneralAdapter);
        spinnerPlaceGeneral.setSelection(CATEGORY_GENERAL.length-1);    // [전체] 를 선택

        ArrayAdapter placeSpecialAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CATEGORY_SPECIAL);
        placeSpecialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaceSpecial.setAdapter(placeSpecialAdapter);
        spinnerPlaceSpecial.setSelection(CATEGORY_SPECIAL.length-1);    // [전체] 를 선택
    }

    // Spinner Arr Setting
    public void dropDownCategoryArrSet(){
        // Spinner ( 드롭다운 메뉴 ) 에 사용될 카테고리의 배열값 할당
        // Static 배열 복사
        System.arraycopy( Global.CATEGORY_GENERAL_STR_ARR, 0, CATEGORY_GENERAL, 0, Global.CATEGORY_GENERAL_LENGTH );
        System.arraycopy( Global.CATEGORY_SPECIAL_STR_ARR, 0, CATEGORY_SPECIAL, 0, Global.CATEGORY_SPECIAL_LENGTH );

        // "전체" 항목 추가
        CATEGORY_GENERAL[CATEGORY_GENERAL.length-1] = "전체";
        CATEGORY_SPECIAL[CATEGORY_SPECIAL.length-1] = "전체";
    }

    // Spinner OnItemClick  ( position 으로 선택 아이템 알 수 있다. )
    public void spinnerSetOnItemClick() {
        // 지역 선택시
        spinnerLocate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LOCATION_IDX = position;
                bus.post(new VPSpinnerItemSelected(LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 반려견 크기 선택시
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SIZE_IDX = position;
                bus.post(new VPSpinnerItemSelected(LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 장소 ( 애견동반 ) 선택시
        spinnerPlaceGeneral.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GENERAL_IDX = position;
                bus.post(new VPSpinnerItemSelected(LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 장소 ( 애견전용 ) 선택시
        spinnerPlaceSpecial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPECIAL_IDX = position;
                bus.post(new VPSpinnerItemSelected(LOCATION_IDX, SIZE_IDX, GENERAL_IDX, SPECIAL_IDX));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
