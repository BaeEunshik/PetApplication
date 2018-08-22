package com.naver.mycnex.viewpageapplication.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Store {
    private long id;
    private String name;
    private Integer contact;
    private Integer dog_size;
    private String store_information;
    private String operation_day;
    private String operation_time;
    private Integer parking;
    private Integer reservation;
    private String address;
    private String sigungu;
    private String dong;
    private double latitude;
    private double longitude;
    private Integer category;

    // DB의 buildingName 삭제. category 항목 추가. ex) 애견카페, 애견미용, 일반카페 등등
}
