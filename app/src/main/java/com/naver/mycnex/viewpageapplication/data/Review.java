package com.naver.mycnex.viewpageapplication.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Review {
    private long id;
    private long store_id;
    private long member_id;
    private String content;

}
