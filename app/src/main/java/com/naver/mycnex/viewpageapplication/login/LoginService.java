package com.naver.mycnex.viewpageapplication.login;

import com.naver.mycnex.viewpageapplication.data.Member;

import java.util.ArrayList;


public class LoginService {
    private static LoginService curr = null;
    private Member loginMember;
    private ArrayList<Member> memberList = new ArrayList<>();

    public static LoginService getInstance() {
        if (curr == null) {
            curr = new LoginService();
        }

        return curr;
    }

    // 로그아웃
    public void logOut() {
        loginMember = null;
    }

    //
    private LoginService() {

    }

    // 멤버 ArrList 를 모두 돌며 - login_id 체크
    public boolean checkId(String login_id) {
        for (int i = 0 ; i < memberList.size() ; i++) {
            if (memberList.get(i).getLogin_id().equals(login_id)) {
                return true;
            }
        }
        return false;
    }

    // 로그인 ( login_id / login_pw 체크 )
    public boolean login(String login_id,String login_pw) {
        for (int i = 0 ; i < memberList.size() ; i++) {
            if (memberList.get(i).getLogin_id().equals(login_id) && memberList.get(i).getLogin_pw().equals(login_pw)) {
                return true;
            }
        }
        return false;
    }

    // 현재 로그인멤버 객체 리턴
    public Member getLoginMember() {
        return loginMember;
    }

    // 로그인멤버 set
    public void setLoginMember(Member loginMember) {
        this.loginMember = loginMember;
    }
    // 멤버 ArrList 리턴
    public ArrayList<Member> getMemberList() {
        return memberList;
    }
    // 멤버 ArrList set
    public void setMemberList(ArrayList<Member> memberList) {
        this.memberList = memberList;
    }
}
