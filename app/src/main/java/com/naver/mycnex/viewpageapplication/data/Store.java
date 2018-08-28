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
    private String contact;
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

    public Store(String name, String contact, Integer dog_size, String store_information, String operation_day, String operation_time, Integer parking, Integer reservation, String address, String sigungu, String dong, double latitude, double longitude, Integer category) {
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
        this.dong = dong;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

    // DB의 buildingName 삭제. category 항목 추가. ex) 애견카페, 애견미용, 일반카페 등등
}
