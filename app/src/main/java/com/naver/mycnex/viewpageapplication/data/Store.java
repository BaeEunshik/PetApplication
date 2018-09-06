package com.naver.mycnex.viewpageapplication.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Store {
    private long id;
    private String name;
    private String contact;
    private Integer dog_size;
    private String store_information;
    private String operation_day;
    private String operation_time;
    private Integer parking;
    private Integer reservation;
    private String address;
    private Integer sigungu;
    private double latitude;
    private double longitude;
    private Integer category;
    private Integer hit;
    private Integer score_sum;
    private Integer score_count;

    public Store(String name, String contact, Integer dog_size, String store_information, String operation_day, String operation_time, Integer parking, Integer reservation, String address, Integer sigungu, double latitude, double longitude, Integer category, Integer hit, Integer score_sum, Integer score_count) {
        this.name = name;
        this.contact = contact;
        this.dog_size = dog_size;
        this.store_information = store_information;
        this.operation_day = operation_day;
        this.operation_time = operation_time;
        this.parking = parking;
        this.reservation = reservation;
        this.address = address;
        this.sigungu = sigungu;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.hit = hit;
        this.score_sum = score_sum;
        this.score_count = score_count;
    }

    // DB의 buildingName 삭제. category 항목 추가. ex) 애견카페, 애견미용, 일반카페 등등
}
