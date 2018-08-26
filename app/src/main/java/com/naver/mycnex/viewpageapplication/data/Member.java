package com.naver.mycnex.viewpageapplication.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class Member {
    private long id;
    private String login_id;
    private String login_pw;
    private String name;

    public Member(String login_id, String login_pw) {
        this.login_id = login_id;
        this.login_pw = login_pw;
    }
}
