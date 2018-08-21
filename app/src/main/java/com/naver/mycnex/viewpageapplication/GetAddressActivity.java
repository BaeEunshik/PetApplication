package com.naver.mycnex.viewpageapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GetAddressActivity extends AppCompatActivity {

    Unbinder unbinder;
    private Handler handler;    //다음 웹뷰 보기 위한 handler
    @BindView(R.id.daum_webview) WebView daum_webView;
    @BindView(R.id.daum_result) TextView daum_result;

    /*** onCreate ***/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_address);
        //버터나이프
        unbinder = ButterKnife.bind(this);
        // WebView 초기화
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {

        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());
        // webview url load. php 파일 주소
        /* daum_webView.loadUrl("http://192.168.25.60:80/daum_address.php"); */

        //Web 에 작성할 js 코드   --------------------------------------------------------
           /**

            <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
            <script>

                new daum.Postcode({
                    oncomplete: function(data) {
                        if(data.userSelectedType=="R"){
                            // userSelectedType : 검색 결과에서 사용자가 선택한 주소의 타입
                            // return type : R - roadAddress, J : jibunAddress
                            // TestApp 은 안드로이드에서 등록한 이름
                            window.TestApp.setAddress(data.zonecode, data.roadAddress, data.buildingName);
                        } else {
                            window.TestApp.setAddress(data.zonecode, data.jibunAddress, data.buildingName);
                        }
                    }
                }).open();

            </script>

            **/
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }

    /** onDestroy **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    @OnClick(R.id.btnGoBack)
    public void setBtnGoBack(){     //뒤로가기

        finish();
    }

}

