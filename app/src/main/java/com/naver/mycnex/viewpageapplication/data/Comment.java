package com.naver.mycnex.viewpageapplication.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class Comment {
    private double id;
    private String comment;
    private Integer board_id;
}
