package com.naver.mycnex.viewpageapplication.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class Review {
    //별점

    private long id;
    private String review;
    private Integer board_id;

}
