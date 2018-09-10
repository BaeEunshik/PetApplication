package com.naver.mycnex.viewpageapplication.data;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ReviewMember {
    private Review reviews;
    private Member members;
}
